package com.example.myproject.common.baseDao;

import com.example.myproject.common.Scheduled.ScheduleJob;
import com.example.myproject.entity.*;
import com.example.myproject.entity.view.PersonMenuView;
import com.example.myproject.entity.view.UserRoleView;
import org.springframework.stereotype.Repository;

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
    public class UserRoleViewDao extends BaseDao<UserRoleView,String>{

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
    public class PersonRoleDao extends BaseDao<PersonRole,String>{

    }
}
