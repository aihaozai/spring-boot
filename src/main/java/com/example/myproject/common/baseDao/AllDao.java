package com.example.myproject.common.baseDao;

import com.example.myproject.common.Scheduled.ScheduleJob;
import com.example.myproject.entity.*;
import com.example.myproject.entity.activiti.ActHiTaskProcess;
import com.example.myproject.entity.view.*;
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
    public class UserDao extends BaseDao<User,String>{

    }

    @Repository
    public class UserRoleDao extends BaseDao<UserRole,String>{

    }

    @Repository
    public class UserLoginViewDao extends BaseDao<UserLoginView,String>{

    }

    @Repository
    public class PersonMenuViewDao extends BaseDao<PersonMenuView,String>{

    }

    @Repository
    public class MenuPermissionDao extends BaseDao<MenuPermission,String>{

    }
    @Repository
    public class MenuDao extends BaseDao<Menu,String>{

    }
    @Repository
    public class ScheduleJobDao extends BaseDao<ScheduleJob,String>{

    }
    @Repository
    public class RoleDao extends BaseDao<Person,String>{

    }
    @Repository
    public class MenuRoleDao extends BaseDao<MenuRole,String>{

    }
    @Repository
    public class ActReModelDao extends BaseDao<ActReModel,String>{

    }
    @Repository
    public class DeplotmentProcdefViewDao extends BaseDao<DeplotmentProcdefView,String>{

    }

    @Repository
    public class OrganDao extends BaseDao<Organ,String>{

    }

    @Repository
    public class OrganRoleDao extends BaseDao<OrganRole,String>{

    }

    @Repository
    public class PermissionRoleDao extends BaseDao<PermissionRole,String>{
        public List<String> findListByJSONObject(String menuId, String roleId){
            String sql = "SELECT role_permission FROM permission_role where role_id = '"+roleId+"' and menu_id = '"+menuId+"'";
            return this.findListBySQL(sql);
        }
    }
    @Repository
    public class MenuPermissionRoleViewDao extends BaseDao<MenuPermissionRoleView,String>{

    }

    @Repository
    public class DictDao extends BaseDao<Dict,String>{

    }
    @Repository
    public class ApplyLeaveDao extends BaseDao<ApplyLeave,String>{

    }

    @Repository
    public class ApplyLeaveViewDao extends BaseDao<ApplyLeaveView,String>{

    }
    @Repository
    public class UserOrganRoleViewDao extends BaseDao<UserOrganRoleView,String>{

    }
    @Repository
    public class ActHiTaskProcessDao extends BaseDao<ActHiTaskProcess,String>{

    }

}
