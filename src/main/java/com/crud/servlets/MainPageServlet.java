package com.crud.servlets;

import com.crud.dataSource.ConnectionManager;
import com.crud.dto.UserDtoDB;
import com.crud.service.impl.UserServiceDB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
@WebServlet("/mainPage")
public class MainPageServlet extends HttpServlet {

    private static final UserServiceDB USER_SERVICE_DB = UserServiceDB.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (session != null) {
            Long activeUserId = (Long) session.getAttribute("activeUserId");
            if (activeUserId != null) {
                String userIdParam = req.getParameter("userId");
                Long userId = userIdParam != null ? Long.parseLong(userIdParam) : activeUserId;

                UserDtoDB userDtoDB = USER_SERVICE_DB.findById(userId);

                if (userDtoDB != null) {
                    req.setAttribute("activeUserId", activeUserId);
                    req.setAttribute("user", userDtoDB);

                    req.getRequestDispatcher("/WEB-INF/jsp/mainPage.jsp").forward(req, resp);
                } else {
                    resp.sendRedirect(req.getContextPath() + "/login");
                }
            } else {
                resp.sendRedirect(req.getContextPath() + "/login");
            }

        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }

    @Override
    public void destroy() {
        ConnectionManager.closeConnection();
    }
}
