package service.twomall;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import bean.twomall.Action;
import bean.twomall.AuthBean;
import bean.twomall.GoodsBean;

public class GoodsNavi {
	private DataAccessObject dao;
	private Action action;

	public GoodsNavi() {

	}

	public Action entrance(HttpServletRequest req) {
		Action action = null;
		String reqValue = req.getRequestURI().substring(req.getContextPath().length() + 1);

		switch (reqValue) {
		case "Search":
			action = this.searchCtl(req);
			break;
		case "GoodsDetail":
			action = this.goodsDetailCtl(req);
			break;
		case "Basket":
			action = this.basketCtl(req);
			break;
		case "BasketList":
			action = this.basketListCtl(req);
			break;
		case "OrderList":
			action = this.orderListCtl(req);
			break;
		case "OrderComplete":
			action = this.orderCompleteCtl(req);
			break;

		}
		return action;
	}

	private Action orderListCtl(HttpServletRequest req) {
		Action action = new Action();
		ArrayList<GoodsBean> orderList = new ArrayList<GoodsBean>();
		boolean actionType = false;
		AuthBean auth = new AuthBean();
		String goodsInfo = new String();

		String page = "orderList.jsp";
		String message = "세션 만료. 다시 로그인 해주세요~"; // 나 아직 메세지 처리안했음.
		dao = new DataAccessObject();
		dao.getConnection();
		if (req.getSession().getAttribute("accessInfo") != null) {
			auth = getCustomerInfo(req.getSession().getAttribute("accessInfo").toString());
			String[] orderInfo = req.getParameter("orderInfo").split(":"); // 레코드 단위로 :로 끊었음.
			for (int index = 0; index < orderInfo.length; index++) {
				String[] goods = orderInfo[index].split(",");
				GoodsBean gb = new GoodsBean();
				gb.setmId(goods[0]);
				gb.setGoCode(goods[1]);
				gb.setSeCode(goods[2]);
				gb.setGoQty(Integer.parseInt(goods[3]));
				orderList.add(getOrderList(gb));
			}
			String orderListTag = makeHtmlOrderList(orderList, goodsInfo);
			req.setAttribute("gInfo", goodsInfo);

			req.setAttribute("orderList", orderListTag);
			req.setAttribute("mName", auth.getmName());
			req.setAttribute("mPhone", auth.getPhone());

		} else {
			req.setAttribute("message", message);
		}

		dao.closeConnection();
		action.setActionType(actionType);
		action.setPage(page);
		return action;
	}

	private Action orderCompleteCtl(HttpServletRequest req) {
		Action action = new Action();
		ArrayList<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		ArrayList<GoodsBean> goodsListComplete = new ArrayList<GoodsBean>();
		boolean isCommit = true;
		boolean actionType = false;
		String page = "orderComplete.jsp";
		String message = "주문이 완료되었습니다.";
		///////////

		String reqOrder = req.getParameter("orderInfo");
		goodsList = reqOrderProcessing(reqOrder, goodsList, "I");

		dao = new DataAccessObject();
		dao.getConnection();
		dao.setAutoCommit(false);

		String orderDate = null;
		if (this.insOrder(goodsList.get(0))) {
			orderDate = this.searchOrderDate(goodsList.get(0));
			for (GoodsBean gb : goodsList) {
				gb.setOrderDate(orderDate);
				if (!this.insOrderDetail(gb)) { // 실패 했다면.
					message = "잠시 후 다시 주문해 주세요.";
					isCommit = false; // 원래 기본값이 true 지만 실패하면 rollback 해줘야한다.
					page = "BasketCheck";
					break;
				}
			}

			// 주문 상태가 I로 상품이 삽입된후 실행되는 것들
			goodsListComplete = reqOrderProcessing(reqOrder, goodsListComplete, "C");
			for (GoodsBean gb : goodsListComplete) { 
				orderDate = this.searchOrderDate(goodsListComplete.get(0));
				gb.setOrderDate(orderDate);
				if (this.setOrderState(goodsListComplete.get(0))) { // 주문 상태 업데이트 해주는것.
					if (this.setOrderDetailState(gb)) {
					}
				}
			}
			if (deleteBasket(goodsList.get(0))) {
			}
		}
		dao.setTransaction(isCommit);
		dao.setAutoCommit(true);
		dao.closeConnection();
		req.setAttribute("message", message);
		action.setActionType(actionType);
		action.setPage(page);

		return action;
	}

	private Action basketListCtl(HttpServletRequest req) {
		action = new Action();
		GoodsBean gb = new GoodsBean();
		boolean isCommit = false;
		boolean actionType = false;
		String page = "basketList.jsp";
		String key = null;

		gb.setmId(req.getParameter("gInfo"));

		dao = new DataAccessObject();
		dao.getConnection();
		dao.setAutoCommit(false);

		key = this.makeHtmlBasket(this.showBasket(gb));

		dao.setTransaction(isCommit);
		dao.setAutoCommit(true);
		dao.closeConnection();

		req.setAttribute("ordersInfo", key);
		action.setActionType(actionType);
		action.setPage(page);
		return action;

	}

	private Action basketCtl(HttpServletRequest req) {
		Action action = new Action();
		GoodsBean gb = new GoodsBean();
		boolean isCommit = false;
		boolean actionType = false;
		String page = "GoodsDetail";
		String message = null;
		boolean mType = false;

		gb.setmId(req.getParameterValues("gInfo")[0]);
		gb.setGoCode(req.getParameterValues("gInfo")[1]);
		gb.setSeCode(req.getParameterValues("gInfo")[2]);
		gb.setGoQty(Integer.parseInt(req.getParameterValues("gInfo")[3]));

		dao = new DataAccessObject();
		dao.getConnection();
		dao.setAutoCommit(false);

		if (this.checkBasket(gb)) {// 만약에 바스켓에 선택한 상품이 있으면
			if (this.updateBasket(gb)) { // 있으면 없데이트
				isCommit = true;
				page += "?code=" + gb.getGoCode() + "&code=" + gb.getSeCode();
				message = "상품이 장바구니에 담겼습니다. 확인하시겠습니까?";
				mType = true;
			} else {
				message = "다시 시도해주세요";

			}
		} else {// 없을때
			if (!this.insertBasket(gb)) {
				message = "다시 한 번 시도해주세요!";
			} else {
				// 장바구니 입력
				isCommit = true;
				page += "?code=" + gb.getGoCode() + "&code=" + gb.getSeCode();
				message = "상품이 장바구니에 담겼습니다. 확인하시겠습니까?";
				mType = true;

			}
		}

		dao.setTransaction(isCommit);
		dao.setAutoCommit(true);
		dao.closeConnection();

		req.setAttribute("message", message);
		req.setAttribute("mType", mType);
		action.setActionType(actionType);
		action.setPage(page);

		return action;
	}

	private String makeHtmlBasket(ArrayList<GoodsBean> goodsList) {
		StringBuffer sb = new StringBuffer();
		int total = 0;
		int amount = 0;
		String gInfo = new String();
		int count = 0;

		sb.append("<table>");

		sb.append("<tr>");
		sb.append("<td colspan=\"2\">상품정보</td>");
		sb.append("<td>가격</td>");
		sb.append("	<td>수량</td>");
		sb.append("	<td>금액</td>");
		sb.append("<td>판매자</td>");
		sb.append("</tr>");

		for (GoodsBean gb : goodsList) {
			count++;
			amount = (gb.getGoPrice() * gb.getGoQty());
			total += amount;
			sb.append("<tr>");
			sb.append(
					"<td><input type=\"checkbox\" name=\"check\" class=\"check\" onChange=\"checkState(this)\"/> <img src=\"image/"
							+ gb.getGoImage() + "\"/></td>");
			sb.append("<td>" + gb.getGoName() + "</td>");
			sb.append("<td>" + gb.getGoPrice() + "</td>");
			sb.append("<td>" + gb.getGoQty() + "</td>");
			sb.append("<td>" + amount + "</td>");
			sb.append("<td>" + gb.getSeName() + "</td>");
			sb.append("</tr>");
			gInfo += (gb.getmId() + "," + gb.getGoCode() + "," + gb.getSeCode() + "," + gb.getGoQty()
					+ (count == goodsList.size() ? "" : ":"));
		}

		sb.append("<tr>");
		sb.append("<td >total</td>");
		sb.append("<td colspan=\"6\">" + total + "</td>");
		sb.append("</tr>");
		sb.append("</table>");
		sb.append("<input type=\"button\" value=\"주문하기\" onClick=\"order(\'" + gInfo + "\')\"/>");

		return sb.toString();

	}

	private Action goodsDetailCtl(HttpServletRequest req) {
		Action action = new Action();
		String page = "goodsDetail.jsp";
		boolean actionType = false;
		ArrayList<GoodsBean> goodsInfo;
		// req --> gb
		GoodsBean gb = new GoodsBean();
		gb.setGoCode(req.getParameterValues("code")[0]);
		gb.setSeCode(req.getParameterValues("code")[1]);
		// dao
		dao = new DataAccessObject();
		dao.getConnection();

		goodsInfo = this.getDetail(gb);// 1개

		req.setAttribute("goodsImage", "image/" + goodsInfo.get(0).getGoImage());
		req.setAttribute("item", goodsInfo.get(0).getGoName());
		req.setAttribute("price", goodsInfo.get(0).getGoPrice());
		req.setAttribute("gInfo", req.getSession().getAttribute("accessInfo") + ":" + goodsInfo.get(0).getGoCode() + ":"
				+ goodsInfo.get(0).getSeCode());
		req.setAttribute("detailImage", "image/" + goodsInfo.get(0).getGoBimage());
		req.setAttribute("seller", goodsInfo.get(0).getSeName());
		dao.closeConnection();

		action.setActionType(actionType);
		action.setPage(page);

		return action;
	}

	private Action searchCtl(HttpServletRequest req) {
		Action action = new Action();
		String page = "goods.jsp";
		boolean actionType = false;

		GoodsBean gb = new GoodsBean();
		// req --> GoodsBean
		gb.setWord(req.getParameter("word"));

		// Connection 생성
		dao = new DataAccessObject();
		dao.getConnection();
		dao.setAutoCommit(false);

		// 상품 조회
		req.setAttribute("gList",
				this.makeGoodsList((gb.getWord().equals("")) ? this.ArraysearchGoods() : this.ArraysearchGoods(gb)));

		dao.setAutoCommit(true);
		dao.closeConnection();

		// Action 설정
		action.setActionType(actionType);
		action.setPage(page);

		return action;
	}

	private String makeGoodsList(ArrayList<GoodsBean> gList) {
		StringBuffer sb = new StringBuffer();
		int index = 0;
		for (GoodsBean goods : gList) {
			index++;
			if (index % 3 == 1) {
				sb.append("<div class=\"line\">");
			} // 3의배수라면
			sb.append("<div class=\"item\" onClick=\"goDetail(\'" + goods.getGoCode() + ":" + goods.getSeCode()
					+ "\')\">"); // 이걸실행
			sb.append("<div class=\"item__top\"><img src=\"image/" + goods.getGoImage() + "\" /></div>");//
			sb.append("<div class=\"item__bottom\"><div class=\"item-name\">" + goods.getGoName() + "</div>"
					+ "<div class=\"item-price\">" + goods.getGoPrice() + "원</div>" + "<div class=\"item-stock\">재고 "
					+ goods.getGoStock() + "&nbsp;&nbsp;&nbsp;무료배송</div></div>");
			sb.append("</div>");

			if (index % 3 == 0) {
				sb.append("</div>");
			}
		}
		if (index % 3 != 0) {
			sb.append("</div>");
		}

		return sb.toString();
	}

	private ArrayList<GoodsBean> getDetail(GoodsBean gb) {
		return dao.getDetail(gb);
	}

	private ArrayList<GoodsBean> ArraysearchGoods() {
		return dao.searchGoods();
	}

	private ArrayList<GoodsBean> ArraysearchGoods(GoodsBean gb) {
		return dao.searchGoods(gb);
	}

	private boolean checkBasket(GoodsBean gb) {
		return convertToBoolean(dao.checkBasket(gb));

	}

	private boolean insertBasket(GoodsBean gb) {
		return convertToBoolean(dao.insertBasket(gb));
	}

	private boolean updateBasket(GoodsBean gb) {
		return convertToBoolean(dao.updateBasket(gb));
	}

	private ArrayList<GoodsBean> showBasket(GoodsBean gb) {
		return dao.showBasket(gb);
	}

	private boolean convertToBoolean(int value) {

		// TODO Auto-generated method stub
		return (value == 1) ? true : false;
	}

	public AuthBean getCustomerInfo(String sessionId) { // 회원정보
		return dao.getCustomerInfo(sessionId);
	}

	private GoodsBean getOrderList(GoodsBean gb) {
		return dao.getOrderList(gb);
	}

	private ArrayList<GoodsBean> reqOrderProcessing(String reqOrder, ArrayList<GoodsBean> goodsList,
			String orderState) {
		String[] orderInfo = reqOrder.split(":");
		for (int index = 0; index < orderInfo.length; index++) {
			String[] goods = orderInfo[index].split(",");
			GoodsBean gb = new GoodsBean();
			gb.setmId(goods[0]);
			gb.setGoCode(goods[1]);
			gb.setSeCode(goods[2]);
			gb.setGoQty(Integer.parseInt(goods[3]));
			gb.setOrderState(orderState);
			gb.setOrderDetailState(orderState);
			goodsList.add(gb);
		}
		return goodsList;

	}

	private boolean insOrder(GoodsBean gb) {
		return convertToBoolean(dao.insOrder(gb));
	}

	private String makeHtmlOrderList(ArrayList<GoodsBean> orderList, String goodsInfo) {
		StringBuffer sb = new StringBuffer();
		int amount = 0;
		int totalAmount = 0;
		int count = 0;
		sb.append("<table border='1px'>");
		sb.append("<tr>");
		sb.append("<th colspan=\"2\">상품정보</th>");
		sb.append("<th>판매자</th>");
		sb.append("<th>수량</th>");
		sb.append("<th>주문 금액</th>");
		sb.append("</tr>");
		for (GoodsBean gb : orderList) {
			count++;
			amount = (gb.getGoPrice() * gb.getGoQty());
			totalAmount += amount;
			sb.append("<tr>");
			sb.append("<td><div class=\"box\"\"><img class=\"profile\" src = \"image/" + gb.getGoImage()
					+ "\"></div></td>");
			sb.append("<td>" + gb.getGoName() + "</td>");
			sb.append("<td>" + gb.getSeName() + "</td>");
			sb.append("<td>" + gb.getGoQty() + "</td>");
			sb.append("<td>" + amount + "</td>");
			sb.append("</tr>");
			goodsInfo += (gb.getmId() + "," + gb.getGoCode() + "," + gb.getSeCode() + "," + gb.getGoQty()
					+ (count == orderList.size() ? "" : ":"));
		}
		sb.append("</table>");
		sb.append("총 금액: " + totalAmount);
		sb.append("<input type=\"button\" value=\"주문하기\" onClick=\"order(\'" + goodsInfo + "\')\"/>");

		return sb.toString();
	}

	private String searchOrderDate(GoodsBean gb) {
		return dao.searchOrderDate(gb);

	}

	private boolean insOrderDetail(GoodsBean gb) {
		return convertToBoolean(dao.insOrderDetail(gb));
	}

	private boolean setOrderState(GoodsBean gb) {
		return convertToBoolean(dao.setOrderState(gb));
	}

	private boolean setOrderDetailState(GoodsBean gb) {
		return convertToBoolean(dao.setOrderDetailState(gb));
	}

	private boolean deleteBasket(GoodsBean gb) {
		return convertToBoolean(dao.deleteBasket(gb));
	}

}
