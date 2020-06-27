package com.example.myproject.common.baseDao;

import com.example.myproject.common.Scheduled.ScheduleJob;
import com.example.myproject.entity.activiti.ActHiTaskProcess;
import com.example.myproject.entity.activiti.ActReModel;
import com.example.myproject.entity.activiti.view.ApplyLeaveView;
import com.example.myproject.entity.business.ApplyLeave;
import com.example.myproject.entity.business.DeploymentProcdefView;
import com.example.myproject.entity.sys.*;
import com.example.myproject.entity.sys.view.MenuPermissionRoleView;
import com.example.myproject.entity.sys.view.PersonMenuView;
import com.example.myproject.entity.sys.view.UserLoginView;
import com.example.myproject.entity.sys.view.UserOrganRoleView;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-08-28 22:26
 **/

@Repository
public class AllDao{
    @Repository
    public static class UserDao extends BaseDao<User,String>{

    }

    @Repository
    public static class UserRoleDao extends BaseDao<UserRole,String>{

    }

    @Repository
    public static class UserLoginViewDao extends BaseDao<UserLoginView,String>{

    }

    @Repository
    public static class PersonMenuViewDao extends BaseDao<PersonMenuView,String>{

    }

    @Repository
    public static class MenuPermissionDao extends BaseDao<MenuPermission,String>{

    }
    @Repository
    public static class MenuDao extends BaseDao<Menu,String>{

    }
    @Repository
    public static class ScheduleJobDao extends BaseDao<ScheduleJob,String>{

    }
    @Repository
    public static class RoleDao extends BaseDao<Person,String>{

    }
    @Repository
    public static class MenuRoleDao extends BaseDao<MenuRole,String>{

    }
    @Repository
    public static class ActReModelDao extends BaseDao<ActReModel,String>{

    }
    @Repository
    public static class DeploymentProcdefViewDao extends BaseDao<DeploymentProcdefView,String>{

    }

    @Repository
    public static class OrganDao extends BaseDao<Organ,String>{

    }

    @Repository
    public static class OrganRoleDao extends BaseDao<OrganRole,String>{

    }

    @Repository
    public static class PermissionRoleDao extends BaseDao<PermissionRole,String>{
        public List<String> findListByJSONObject(String menuId, String roleId){
            String sql = "SELECT role_permission FROM permission_role where role_id = '"+roleId+"' and menu_id = '"+menuId+"'";
            return this.findListBySQL(sql);
        }
    }
    @Repository
    public static class MenuPermissionRoleViewDao extends BaseDao<MenuPermissionRoleView,String>{

    }

    @Repository
    public static class DictDao extends BaseDao<Dict,String>{

    }
    @Repository
    public static class ApplyLeaveDao extends BaseDao<ApplyLeave,String>{

    }

    @Repository
    public static class ApplyLeaveViewDao extends BaseDao<ApplyLeaveView,String>{

    }
    @Repository
    public static class UserOrganRoleViewDao extends BaseDao<UserOrganRoleView,String>{

    }
    @Repository
    public static class ActHiTaskProcessDao extends BaseDao<ActHiTaskProcess,String>{

    }

}
