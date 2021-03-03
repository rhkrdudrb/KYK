package controller.twomall;


import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.twomall.Action;
import search.twomall.GoodsSearch;
import service.twomall.Authentication;


@WebServlet({"/Main", "/LogInForm", "/LogIn",  
			"/LogOut","/FindForm","/FindForm2"})
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public FrontController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.adaptiveRouting(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		this.adaptiveRouting(request, response);
	}
	
	private void adaptiveRouting(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		Action action = null;
		Authentication auth = null;
		GoodsSearch goods = null;
						
		String reqValue = 
				req.getRequestURI().substring(req.getContextPath().length() + 1);
		
		
		if(req.getMethod().equals("GET")) {
			if(reqValue.equals("GoodsDetail")) {
				goods = new GoodsSearch();
				action = goods.entrance(req);
			}else {
				action = new Action();
				action.setActionType(true);
				action.setPage("main.jsp");
				action.setMessage("잘못된 접근입니다. 메인페이지로 이동합니다.");
			}
		} else {
			// Post Type
			if(reqValue.equals("LogInForm")) {
				action = new Action();
				action.setActionType(false);
				action.setPage("login.jsp");
			}else if(reqValue.equals("Main")){
				action = new Action();
				action.setActionType(false);
				action.setPage("main.jsp");
			}else if(reqValue.equals("LogIn")) {
				auth = new Authentication();
				action = auth.entrance(req);
			}else if(reqValue.equals("LogOut")) {
				auth = new Authentication();
				action = auth.entrance(req);
			}else if(reqValue.equals("FindForm")) {
				auth = new Authentication();
				action = auth.entrance(req);
			}else if(reqValue.equals("FindForm2")) {
				auth = new Authentication();
				action = auth.entrance(req);
			req.setAttribute("prev", req.getParameter("page"));
		}
		
		// 서버 응답
		if(action.isActionType()) {
			if(action.getMessage() != null) {
				res.sendRedirect(action.getPage() + "?message=" + URLEncoder.encode(action.getMessage(), "UTF-8"));
			}else {
				res.sendRedirect(action.getPage());
			}
			
		}else {
			RequestDispatcher dispatcher = req.getRequestDispatcher(action.getPage());
			dispatcher.forward(req, res);
		}
	}
	
}
}
