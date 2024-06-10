package com.crud.servlets;

import com.crud.dataSource.ConnectionManager;
import com.crud.dto.UserDtoDB;
import com.crud.exceptions.ServletCrudException;
import com.crud.service.impl.UserService;
import com.crud.service.impl.UserServiceDB;
import com.crud.util.Constants;
import com.crud.util.JspHelper;
import com.crud.util.UrlPath;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.logging.Logger;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());
    private static final UserServiceDB USER_SERVICE = UserServiceDB.getInstance();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.getRequestDispatcher("WEB-INF/jsp/login.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            LOGGER.severe("Error while processing request");
            throw new ServletCrudException("Error while processing request", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        USER_SERVICE.login(req.getParameter("login"))
                .ifPresentOrElse(
                        user -> onLoginSuccess(user, req, resp),
                        () -> onLoginFail(req, resp)
                );

    }

    private void onLoginSuccess(UserDtoDB user, HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(true);
        Long id = user.getId();
        session.setAttribute("activeUserId", id);
        session.setAttribute("activeUser", user);
        req.setAttribute("user", user);
        try {
            resp.sendRedirect("/mainPage");
        } catch (IOException e) {
            LOGGER.severe("Error while redirecting to main page");
            req.setAttribute("errorMessage", e.getMessage());
            try {
                req.getRequestDispatcher("/WEB_INF/jsp/login.jsp").forward(req, resp);
            } catch (ServletException | IOException ex) {
                LOGGER.severe("Error while redirecting to login page");
                throw new ServletCrudException("Error while redirecting to main page", ex);
            }
        }
    }

    private void onLoginFail(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.sendRedirect(("/login?error=true&email=" + req.getParameter("email")));
        } catch (IOException e) {
            LOGGER.severe("Error while redirecting to login page");
            throw new ServletCrudException("Error while redirecting to login page", e);
        }
    }


}
