package com.dataart.playme.listener;

import com.dataart.playme.exception.ApplicationInitializationException;
import com.dataart.playme.repository.JDBCUserRepository;
import com.dataart.playme.repository.impl.JDBCUserRepositoryImpl;
import com.dataart.playme.service.AuthorizationService;
import com.dataart.playme.service.UserService;
import com.dataart.playme.service.UserValidationService;
import com.dataart.playme.service.impl.AuthorizationServiceImpl;
import com.dataart.playme.service.impl.UserServiceImpl;
import com.dataart.playme.service.impl.UserValidationServiceImpl;
import com.dataart.playme.util.JDBCConnector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

@WebListener
public class ContextListener implements ServletContextListener {

    private static final String DRIVER_NAME = "org.postgresql.Driver";

    private static final String CONTEXT_NAME = "jdbc/playme";

    private static final String ENVIRONMENT_CONTEXT_NAME = "java:comp/env";

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        init(context);
    }

    private void init(ServletContext context) {
        DataSource dataSource = getDataSource();
        JDBCConnector.setDataSource(dataSource);

        //init repositories
        JDBCUserRepository userRepository = new JDBCUserRepositoryImpl();

        //init services
        UserValidationService userValidationService = new UserValidationServiceImpl();
        UserService userService = new UserServiceImpl(userRepository, userValidationService);
        AuthorizationService authorizationService = new AuthorizationServiceImpl();

        //save services
        context.setAttribute(UserService.class.getName(), userService);
        context.setAttribute(AuthorizationService.class.getName(), authorizationService);
    }

    private DataSource getDataSource() {
        try {
            Class.forName(DRIVER_NAME);
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup(ENVIRONMENT_CONTEXT_NAME);
            return (DataSource) envContext.lookup(CONTEXT_NAME);
        } catch (ClassNotFoundException e) {
            throw new ApplicationInitializationException("Can't find appropriate driver", e);
        } catch (NamingException e) {
            throw new ApplicationInitializationException("Can't initialize database", e);
        }
    }
}
