package com.siam.package_user.service_impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.constant.BusinessType;
import com.siam.package_common.exception.StoneCustomerException;
import com.siam.package_common.util.Base64Utils;
import com.siam.package_common.util.CommonUtils;
import com.siam.package_common.util.RedisUtils;
import com.siam.package_user.auth.cache.AdminSessionManager;
import com.siam.package_user.entity.Admin;
import com.siam.package_user.mapper.AdminMapper;
import com.siam.package_user.model.example.AdminExample;
import com.siam.package_user.model.param.AdminParam;
import com.siam.package_user.model.result.AdminResult;
import com.siam.package_user.service.AdminService;
import com.siam.package_user.service.MemberTradeRecordService;
import com.siam.package_user.util.TokenUtil;
import com.siam.package_util.entity.SmsLog;
import com.siam.package_util.feign.SmsLogFeignApi;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminSessionManager adminSessionManager;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private SmsLogFeignApi smsLogFeignApi;

    @Autowired
    private MemberTradeRecordService memberTradeRecordService;

    @Autowired
    private RedisUtils redisUtils;

    public int countByExample(AdminExample example){
        return adminMapper.countByExample(example);
    }

    public void insertSelective(Admin record){
        adminMapper.insertSelective(record);
    }

    public List<Admin> selectByExample(AdminExample example){
        return adminMapper.selectByExample(example);
    }

    public Admin selectByPrimaryKey(Integer id){
        return adminMapper.selectByPrimaryKey(id);
    }

    public void updateByExampleSelective(Admin record, AdminExample example){
        adminMapper.updateByExampleSelective(record, example);
    }

    public void updateByPrimaryKeySelective(Admin record){
        adminMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public String encryptionBySalt(String password, String passwordSalt) {
        // 生成盐

        return null;
    }

    @Override
    public Page getListByPage(AdminParam param) {
        Page<Map<String, Object>> page = adminMapper.getListByPage(new Page(param.getPageNo(), param.getPageSize()), param);
        return page;
    }

    @Override
    public Admin selectByUsername(String username) {
        return adminMapper.selectByUsername(username);
    }

    @Override
    public AdminResult login(AdminParam param) {
        Admin dbAdmin = adminMapper.selectByUsername(param.getUsername());
        if(dbAdmin == null){
            throw new StoneCustomerException("该账号不存在");
        }

        // 判断密码是否匹配
        String password = Base64Utils.decode(param.getPassword());
        password = CommonUtils.genMd5Password(password, dbAdmin.getPasswordSalt());
        if(!password.equals(dbAdmin.getPassword())){
            throw new StoneCustomerException("密码错误");
        }

        // 更新管理员登录信息
        Admin updateAdmin = new Admin();
        updateAdmin.setId(dbAdmin.getId());
        updateAdmin.setLastLoginTime(new Date());
        adminMapper.updateByPrimaryKeySelective(updateAdmin);

        // 生成token
        String token = TokenUtil.generateToken(dbAdmin);

        //创建登录会话
        adminSessionManager.createSession(token, dbAdmin);

        //创建登录cookie
        TokenUtil.addLoginCookie(token);

        AdminResult result = new AdminResult();
        result.setToken(token);
        return result;
    }

    @Override
    public AdminResult loginByMobile(AdminParam param) {
        String systemMobileCode = (String) redisUtils.get("systemMobileCode");
        if(StringUtils.isNotBlank(systemMobileCode) && systemMobileCode.equals(param.getMobileCode())) {
            //万能验证码，放行
        }else {
            // 判断验证码是否匹配
            SmsLog smsLog = smsLogFeignApi.getLastLog(param.getMobile(), BusinessType.SMS_LOG_TYPE_LOGIN, 5).getData();
            if (smsLog==null || !smsLog.getVerifyCode().equals(param.getMobileCode())) {
                throw new StoneCustomerException("手机验证码错误");
            }
        }

        // 判断是否已经注册
        Admin dbAdmin = adminMapper.selectByMobile(param.getMobile());
        if(dbAdmin == null){
            throw new StoneCustomerException("该账号不存在");
        }

        // 生成token
        String token = TokenUtil.generateToken(dbAdmin);

        //创建登录会话
        adminSessionManager.createSession(token, dbAdmin);

        //创建登录cookie
        TokenUtil.addLoginCookie(token);

        AdminResult result = new AdminResult();
        result.setToken(token);
        return result;
    }

    @Override
    public AdminResult getLoginAdminInfo(AdminParam param) {
        Admin loginAdmin = adminSessionManager.getSession(TokenUtil.getToken());

        Admin dbAdmin = adminMapper.selectByPrimaryKey(loginAdmin.getId());

        AdminResult result = new AdminResult();
        BeanUtils.copyProperties(dbAdmin, result);
        return result;
    }

    @Override
    public void updatePassword(AdminParam param) {
        Admin loginAdmin = adminSessionManager.getSession(TokenUtil.getToken());
        Admin dbAdmin = adminMapper.selectByPrimaryKey(loginAdmin.getId());

        //Base64解密
        String oldPassword = Base64Utils.decode(param.getOldPassword());
        String newPassword = Base64Utils.decode(param.getNewPassword());

        String passwordSalt = dbAdmin.getPasswordSalt();
        oldPassword = CommonUtils.genMd5Password(oldPassword, passwordSalt);
        if(!dbAdmin.getPassword().equals(oldPassword)){
            throw new StoneCustomerException("原密码错误");
        }

        if(StringUtils.isNotBlank(newPassword) && newPassword.length()<6){
            throw new StoneCustomerException("密码长度最少6位");
        }

        newPassword = CommonUtils.genMd5Password(newPassword, passwordSalt);

        //修改管理员密码
        Admin updateAdmin = new Admin();
        updateAdmin.setId(dbAdmin.getId());
        updateAdmin.setPassword(newPassword);
        adminMapper.updateByPrimaryKeySelective(updateAdmin);
    }

    @Override
    public void logout(AdminParam param) {
        Admin loginAdmin = adminSessionManager.getSession(TokenUtil.getToken());

//        //记录退出日志
//        LogManager.me().executeLog(LogTaskFactory.exitLog(LoginContextHolder.getContext().getUser().getId(), getIp()));

        //删除Auth相关cookies
        TokenUtil.deleteLoginCookie();

        //删除会话
        adminSessionManager.removeSession(TokenUtil.getToken());
    }

    @Override
    public void forgetPasswordStep1(AdminParam param) {
        String systemMobileCode = (String) redisUtils.get("systemMobileCode");
        if(StringUtils.isNotBlank(systemMobileCode) && systemMobileCode.equals(param.getMobileCode())) {
            //万能验证码，放行
        }else {
            // 判断验证码是否匹配
            SmsLog smsLog = smsLogFeignApi.getLastLog(param.getMobile(), BusinessType.SMS_LOG_TYPE_FINDPWD, 5).getData();
            if (smsLog==null || !smsLog.getVerifyCode().equals(param.getMobileCode())) {
                throw new StoneCustomerException("手机验证码错误");
            }
        }

        //TODO-将验证码改为已验证

        // 判断是否已经注册
        Admin dbAdmin = adminMapper.selectByUsername(param.getMobileCode());
        if(dbAdmin == null){
            throw new StoneCustomerException("该账号不存在");
        }
    }

    @Override
    public void forgetPasswordStep2(AdminParam param) {
        //TODO-长时间未操作，请返回第一步重新操作

        // 判断是否已经注册
        Admin dbAdmin = adminMapper.selectByMobile(param.getMobile());
        if(dbAdmin == null){
            throw new StoneCustomerException("该账号不存在");
        }

        //Base64解密
        String password = Base64Utils.decode(param.getPassword());
        String passwordSalt = dbAdmin.getPasswordSalt();
        if(StringUtils.isNotBlank(password) && password.length()<6){
            throw new StoneCustomerException("密码长度最少6位");
        }
        password = CommonUtils.genMd5Password(password, passwordSalt);

        //修改商家密码
        Admin updateAdmin = new Admin();
        updateAdmin.setId(dbAdmin.getId());
        updateAdmin.setPassword(password);
        adminMapper.updateByPrimaryKeySelective(updateAdmin);
    }
}