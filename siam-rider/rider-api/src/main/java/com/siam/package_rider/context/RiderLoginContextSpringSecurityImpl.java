
package com.siam.package_rider.context;

import cn.hutool.core.util.ObjectUtil;
import com.siam.package_common.context.login.RiderLoginContext;
import com.siam.package_common.exception.AuthException;
import com.siam.package_common.exception.enums.AuthExceptionEnum;
import com.siam.package_common.pojo.login.SysLoginRider;
import com.siam.package_rider.cache.RiderSessionManager;
import com.siam.package_rider.entity.Rider;
import com.siam.package_rider.util.TokenUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 登录用户上下文实现类
 *
 * @author
 * @date 2020/3/13 12:19
 */
@Component
public class RiderLoginContextSpringSecurityImpl implements RiderLoginContext {

    @Autowired
    private RiderSessionManager riderSessionManager;

    private RiderLoginContextSpringSecurityImpl() {

    }

    /**
     * 获取当前登录用户
     *
     * @author
     * @date 2020/3/13 14:42
     */
    @Override
    public SysLoginRider getSysLoginRider() {
        Rider loginRider = riderSessionManager.getSession(TokenUtil.getToken());
        if (ObjectUtil.isEmpty(loginRider)) {
            throw new AuthException(AuthExceptionEnum.LOGIN_EXPIRED);
        } else {
            SysLoginRider sysLoginRider = new SysLoginRider();
            BeanUtils.copyProperties(loginRider, sysLoginRider);
            return sysLoginRider;
        }
    }

    /**
     * 管理员类型（0超级管理员 1非管理员）
     * 判断当前登录用户是否是超级管理员
     *
     * @author
     * @date 2020/3/23 17:51
     */
    @Override
    public boolean isSuperAdmin() {
        //TODO - 临时限制
        /*return this.isRider(RiderTypeEnum.SUPER_ADMIN.getCode());*/
        return getSysLoginRider().getUsername().startsWith("admin-");
    }
}