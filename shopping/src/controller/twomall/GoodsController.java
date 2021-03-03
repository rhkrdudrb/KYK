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
import service.twomall.Authentication;
import service.twomall.GoodsNavi;


@WebServlet({"/GoodsDetail","/Search","/Basket","/BasketList", "/OrderList", "/OrderComplete"})
public class GoodsController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public GoodsController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.adaptiveRouting(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		this.adaptiveRouting(request, response);
	}

	private void adaptiveRouting(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		Action action = null;
		GoodsNavi goods = null;
		Authentication auth =new Authentication();
		String reqValue = req.getRequestURI().substring(req.getContextPath().length() + 1);

		if (req.getMethod().equals("GET")) {
			if(reqValue.equals("GoodsDetail")) {
				goods = new GoodsNavi();
				action = goods.entrance(req);
			}
		} else {
			// Post Type
			 if (reqValue.equals("Search") || reqValue.equals("Basket") || reqValue.equals("BasketList") || reqValue.equals("OrderList") || reqValue.equals("OrderComplete")) {
				goods = new GoodsNavi();
				action = goods.entrance(req);
			}else if (reqValue.equals("GoodsDetail")) {
				goods = new GoodsNavi();
				action = goods.entrance(req);
				req.setAttribute("prev", req.getParameter("page"));
			}
			 req.setAttribute("prev", req.getParameter("page")); //?? 잘수정햇나.. 다시확인.

			// 서버 응답
			if (action.isActionType()) {
				if (action.getMessage() != null) {
					res.sendRedirect(action.getPage() + "?message=" + URLEncoder.encode(action.getMessage(), "UTF-8"));
				} else {
					res.sendRedirect(action.getPage());
				}

			} else {
				RequestDispatcher dispatcher = req.getRequestDispatcher(action.getPage());
				dispatcher.forward(req, res);
			}
		}

	}
}