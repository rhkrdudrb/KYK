package service.twomall;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import bean.twomall.Action;
import bean.twomall.AuthBean;

public class Authentication {
	private DataAccessObject dao;
	
	public Authentication() {
		
	}
	
	public Action entrance(HttpServletRequest req) throws UnsupportedEncodingException {
		Action action = null;
		String reqValue = 
				req.getRequestURI().substring(req.getContextPath().length() + 1);
		switch(reqValue) {
		case "LogIn":
			action = this.logInCtl(req);
			break;
		case "LogOut":
			action = this.logOutCtl(req);
			break;
		case"FindForm" :
			action = this.findCtl(req);
			break;
		case"FindForm2" :
			action = this.find2Ctl(req);
			break;
		}
		return action;
	}
	
	private Action find2Ctl(HttpServletRequest req) {
		Action action = new Action();
			boolean actionType = false;
			String page = "find2.jsp";
			String message = new String();
			AuthBean auth = new AuthBean();
			
			
			auth.setmId((req.getParameterValues("accessInfo")[0]));
			auth.setPhone((req.getParameterValues("accessInfo")[1]));
			
			
			dao = new DataAccessObject();
			dao.getConnection();
		
			message=dao.findPw(auth);
			
		
			dao.setAutoCommit(true);
			dao.closeConnection();
						 
			req.setAttribute("message", message);
			
			
			action.setActionType(actionType);
			action.setPage(page);
			
			
		return action;
	}
	
	
	private Action findCtl(HttpServletRequest req) {
		Action action = new Action();
			boolean actionType = false;
			String page = "find.jsp";
			String message = new String();
			AuthBean auth = new AuthBean();
			
			
			auth.setmName((req.getParameterValues("accessInfo")[0]));
			auth.setPhone((req.getParameterValues("accessInfo")[1]));
			
			
			dao = new DataAccessObject();
			dao.getConnection();
		
			message=dao.findId(auth);
			
			
			dao.setAutoCommit(true);
			dao.closeConnection();
						 
			req.setAttribute("message", message);
			
			
			action.setActionType(actionType);
			action.setPage(page);
			
			
		return action;
	}
	
	private Action logOutCtl(HttpServletRequest req){
		Action action = new Action();
		boolean tran = false;
		boolean actionType = true;
		String page = "index.jsp";
		String message = null;
		
		AuthBean auth = new AuthBean();
		auth.setmId(req.getParameter("member"));
		auth.setAccessType(-1);
		
		dao = new DataAccessObject();
		
		dao.getConnection();
		dao.setAutoCommit(false);
		
		HttpSession session = req.getSession();
		if(session.getAttribute("accessInfo") != null) {
			if(session.getAttribute("accessInfo").equals(auth.getmId())) {

				if(this.insertLog(auth)) {
					session.removeAttribute("accessInfo");

					tran = true;
					message = "정상적으로 로그아웃되었습니다.";
				}

			}
		}else {	message = "이미 로그아웃되었거나 로그인하지 않았습니다.";}
		
		dao.setTransaction(tran);
		dao.setAutoCommit(true);
		dao.closeConnection();
		
		action.setActionType(actionType);
		action.setPage(page);
		action.setMessage(message);
		
		return action;
	}
	
	
	private Action logInCtl(HttpServletRequest req) throws UnsupportedEncodingException {
		Action action = new Action();
		ArrayList<AuthBean> memberInfo = null;
		boolean tran = false;
		boolean actionType = true;
		String page = "login.jsp";
		String message = "아이디나 패스워드가 올바르지 않습니다.";
		
		AuthBean auth = new AuthBean();
		auth.setmId(req.getParameterValues("accessInfo")[0]);
		auth.setmPassword(req.getParameterValues("accessInfo")[1]);
		auth.setAccessType(1);
		
		dao = new DataAccessObject();
		dao.getConnection();
		dao.setAutoCommit(false);
		
		if(this.isMember(auth)) {
			if(this.isActive(auth)) {
				if(this.isAccess(auth)) {
					if(this.insertLog(auth)) {								
						memberInfo = this.searchMemberInfo(auth);
						
						HttpSession session = req.getSession();
						session.setAttribute("accessInfo", memberInfo.get(0).getmId());
												
						tran = true;
						actionType = false;
						page = "Search?word=";  //Search?word=";
						message = "";
						
						if(req.getParameter("action").equals("Basket")) {
							req.setAttribute("gInfo", session.getAttribute("accessInfo"));
							page = "Basket?" + this.setParam("gInfo", req);
							System.out.println(page);
						}
					} else { message = "다시 접속해주세요";}
				}
			}else { message = "휴먼계정입니다. 활성화를 부탁드립니다.";}
		}
		dao.setTransaction(tran);
		dao.setAutoCommit(true);
		dao.closeConnection();
		
		
		String mInfo = memberInfo.get(0).getmName() + "(" + 
				memberInfo.get(0).getmId() +")님은 " + memberInfo.get(0).getAccessTime() +
				" 로그인 하셨습니다.";
		req.setAttribute("memberInfo", mInfo);
		
		action.setActionType(actionType);
		action.setPage(page);
		return action;
	}
	
	private String setParam(String paramName, HttpServletRequest req) throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		
		for(int index=0; index<req.getParameterValues(paramName).length; index++) {
			sb.append(paramName + "=");
			sb.append(URLEncoder.encode((index == 0)? 
					req.getAttribute(paramName).toString(): req.getParameterValues(paramName)[index], "UTF-8"));
	
			sb.append(index==req.getParameterValues(paramName).length-1?"":"&");
		}
		return sb.toString(); 
	}
	
	private boolean isMember(AuthBean auth) {
		return this.convertToBoolean(dao.isMember(auth));
	}
	
	private boolean isActive(AuthBean auth) {
		return this.convertToBoolean(dao.isActive(auth));
	}
	
	private boolean isAccess(AuthBean auth) {
		return this.convertToBoolean(dao.isAccess(auth));
	}
	
	private boolean insertLog(AuthBean auth) {
		return this.convertToBoolean(dao.insAccessLog(auth));
	}
	
	private ArrayList<AuthBean> searchMemberInfo(AuthBean auth){
		return dao.searchMemberInfo(auth);
	}
	
	private boolean convertToBoolean(int value) {
		return (value==1)? true : false;
	}
}

