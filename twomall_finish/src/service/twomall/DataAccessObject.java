package service.twomall;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import bean.twomall.AuthBean;
import bean.twomall.GoodsBean;

/* Database 연동
 * 1. Oracle Driver loading >> Class.forName("oracle.jdbc.driver.OracleDriver");
 * 2. Connection >> DriverManager.getConnection(Oracle Url, Oracle User, password);
 *    - Oracle Url >> "jdbc:oracle:thin:@106.243.194.230:7007:xe"
 * 3. Statement --> 많은 레코드를 추출(where 절이 없는 경우 유용) 
 * 	  PreparedStatement --> Where절이 있는 경우 유용
 * 4. execute --> query -->  ResultSet  --> Record단위로 Bean에 저장  --> ArrayList
 * 	  executeUpdate --> DML  --> 적용된 레코드 수를 int로 리턴
 * 5. Transcation
 * 6. Connection.close()
 * */
class DataAccessObject extends bean.twomall.DataAccessObject {

	DataAccessObject() {

	}

	String findPw(AuthBean auth) {

		String count = new String();

		String query = "select mm_password as PW from mm where mm_id=? and mm_phone=?";
		try {
			this.pstatement = this.connection.prepareStatement(query);
			this.pstatement.setNString(1, auth.getmId());
			this.pstatement.setNString(2, auth.getPhone());

			this.rs = this.pstatement.executeQuery();
			while (rs.next()) {
				count = rs.getNString("PW");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return count;

	}

	String findId(AuthBean auth) {

		String count = new String();

		String query = "select mm_id as ID from mm where mm_name=? and mm_phone=?";
		try {
			this.pstatement = this.connection.prepareStatement(query);
			this.pstatement.setNString(1, auth.getmName());
			this.pstatement.setNString(2, auth.getPhone());

			this.rs = this.pstatement.executeQuery();
			while (rs.next()) {
				count = rs.getNString("ID");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return count;

	}

	// 아이디 유무 확인 + 아이디 중복 체크
	int isMember(AuthBean auth) {
		pstatement = null;
		this.rs = null;
		int count = 0;

		String query = "SELECT COUNT(*) AS CNT FROM MM WHERE MM_ID = ?";
		try {
			this.pstatement = this.connection.prepareStatement(query);
			this.pstatement.setNString(1, auth.getmId());

			this.rs = this.pstatement.executeQuery();
			while (rs.next()) {
				count = rs.getInt("CNT");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return count;
	}

	// 현재 id 활성화 여부 확인
	int isActive(AuthBean auth) {
		this.pstatement = null;
		this.rs = null;
		int count = 0;
		String query = "SELECT COUNT(*) AS CNT FROM MM WHERE MM_ID = ? AND MM_STATE = ?";

		try {
			this.pstatement = this.connection.prepareStatement(query);
			this.pstatement.setNString(1, auth.getmId());
			this.pstatement.setNString(2, "A");

			this.rs = this.pstatement.executeQuery();
			while (rs.next()) {
				count = rs.getInt("CNT");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return count;
	}

	// userId + userPassword 일치여부
	int isAccess(AuthBean auth) {
		this.pstatement = null;
		this.rs = null;
		int count = 0;
		String query = "SELECT COUNT(*) AS CNT FROM MM WHERE MM_ID = ? AND MM_PASSWORD = ?";

		try {
			this.pstatement = this.connection.prepareStatement(query);
			this.pstatement.setNString(1, auth.getmId());
			this.pstatement.setNString(2, auth.getmPassword());

			this.rs = this.pstatement.executeQuery();
			while (rs.next()) {
				count = rs.getInt("CNT");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return count;
	}

	// ACCESSLOG INS
	int insAccessLog(AuthBean auth) {
		this.pstatement = null;
		int count = 0;

		String dml = "INSERT INTO AL(AL_ID, AL_TIME, AL_TYPE) VALUES(?, DEFAULT, ?)";

		try {
			this.pstatement = this.connection.prepareStatement(dml);
			this.pstatement.setNString(1, auth.getmId());
			this.pstatement.setInt(2, auth.getAccessType());

			count = this.pstatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return count;
	}

	// 회원정보 추출 :: 회원아이디, 회원이름, 로그인시간
	ArrayList<AuthBean> searchMemberInfo(AuthBean auth) {
		ArrayList<AuthBean> memberList = new ArrayList<AuthBean>();

		this.pstatement = null;
		this.rs = null;
		String query = "SELECT * FROM MF WHERE AID=? AND ATYPE=?";

		try {
			this.pstatement = this.connection.prepareStatement(query);
			this.pstatement.setNString(1, auth.getmId());
			this.pstatement.setInt(2, auth.getAccessType());

			this.rs = this.pstatement.executeQuery();
			while (rs.next()) {
				AuthBean ab = new AuthBean();
				ab.setmId(rs.getNString("AID"));
				ab.setmName(rs.getNString("MNAME"));
				ab.setAccessTime(rs.getNString("ALTIME"));

				memberList.add(ab);
			}
		} catch (SQLException e) {
		}

		return memberList;
	}

	int isLogIn(AuthBean auth) {
		this.pstatement = null;
		int count = 0;

		String query = "SELECT SUM(AL_TYPE) AS CNT FROM AL WHERE AL_ID = ?";

		try {
			this.pstatement = this.connection.prepareStatement(query);
			this.pstatement.setNString(1, auth.getmId());

			this.rs = this.pstatement.executeQuery();
			while (rs.next()) {
				count = rs.getInt("CNT");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}

	// 전체상품조회
	ArrayList<GoodsBean> searchGoods() {
		ArrayList<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		String query = "SELECT GOCODE,GONAME,SECODE,PRICE,STOCK,LIMAGE FROM GOODSINFO";
		try {
			this.statement = this.connection.createStatement();
			this.rs = this.statement.executeQuery(query);
			while (rs.next()) {
				GoodsBean goods = new GoodsBean();
				goods.setGoCode(rs.getNString("GOCODE"));
				goods.setGoName(rs.getNString("GONAME"));
				goods.setGoPrice(rs.getInt("PRICE"));
				goods.setGoStock(rs.getInt("STOCK"));
				goods.setSeCode(rs.getNString("SECODE"));
				goods.setGoImage(rs.getNString("LIMAGE"));
				goodsList.add(goods);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return goodsList;
	}

	// 단어 검색 상품 조회
	ArrayList<GoodsBean> searchGoods(GoodsBean gb) {
		ArrayList<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		String query = "SELECT GOCODE,GONAME,SECODE,PRICE,STOCK,LIMAGE FROM GOODSINFO WHERE SEARCH LIKE '%' ||?||'%'";

		try {
			this.pstatement = this.connection.prepareStatement(query);
			this.pstatement.setNString(1, gb.getWord());

			this.rs = this.pstatement.executeQuery();

			while (rs.next()) {
				GoodsBean goods = new GoodsBean();
				goods.setGoCode(rs.getNString("GOCODE"));
				goods.setGoName(rs.getNString("GONAME"));
				goods.setGoPrice(rs.getInt("PRICE"));
				goods.setGoStock(rs.getInt("STOCK"));
				goods.setSeCode(rs.getNString("SECODE"));
				goods.setGoImage(rs.getNString("LIMAGE"));
				goodsList.add(goods);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return goodsList;
	}

	// 상세정보 검색
	ArrayList<GoodsBean> getDetail(GoodsBean gb) {
		ArrayList<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		String query = "SELECT GOCODE,GONAME,SECODE,PRICE,STOCK," + "LIMAGE,BIMAGE, SENAME " + "FROM GOODSINFO "
				+ "WHERE GOCODE=? AND SECODE=?";

		try {
			this.pstatement = this.connection.prepareStatement(query);
			this.pstatement.setNString(1, gb.getGoCode());
			this.pstatement.setNString(2, gb.getSeCode());

			this.rs = this.pstatement.executeQuery();
			while (rs.next()) {
				GoodsBean goods = new GoodsBean();
				goods.setGoCode(rs.getNString("GOCODE"));
				goods.setGoName(rs.getNString("GONAME"));
				goods.setGoPrice(rs.getInt("PRICE"));
				goods.setGoStock(rs.getInt("STOCK"));
				goods.setGoImage(rs.getNString("LIMAGE"));
				goods.setGoBimage(rs.getNString("BIMAGE"));
				goods.setSeCode(rs.getNString("SECODE"));
				goods.setSeName(rs.getNString("SENAME"));
				goodsList.add(goods);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return goodsList;
	}

	int checkBasket(GoodsBean gb) {
		int count = 0;

		String query = " SELECT COUNT(*) AS CN FROM BA WHERE BA_MMID=? AND BA_SAGOCODE=? AND BA_SASECODE=? ";

		try {
			this.pstatement = this.connection.prepareStatement(query);
			this.pstatement.setNString(1, gb.getmId());
			this.pstatement.setNString(2, gb.getGoCode());
			this.pstatement.setNString(3, gb.getSeCode());
			rs = this.pstatement.executeQuery();
			while (rs.next()) {
				count = rs.getInt("CN");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}

	int insertBasket(GoodsBean gb) {
		int count = 0;
		String query = "INSERT INTO BA(BA_MMID, BA_SAGOCODE, BA_SASECODE, BA_QUANTITY) VALUES(?, ?, ?, ?)";

		try {
			this.pstatement = this.connection.prepareStatement(query);
			this.pstatement.setNString(1, gb.getmId());
			this.pstatement.setNString(2, gb.getGoCode());
			this.pstatement.setInt(4, gb.getGoQty());
			this.pstatement.setNString(3, gb.getSeCode());

			count = this.pstatement.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return count;

	}

	int updateBasket(GoodsBean gb) {

		int count = 0;
		String query = "UPDATE BA SET BA_QUANTITY = BA_QUANTITY+? WHERE BA_MMID=? AND BA_SAGOCODE=? AND BA_SASECODE=?";

		try {
			this.pstatement = this.connection.prepareStatement(query);
			this.pstatement.setInt(1, gb.getGoQty());
			this.pstatement.setNString(2, gb.getmId());
			this.pstatement.setNString(3, gb.getGoCode());
			this.pstatement.setNString(4, gb.getSeCode());

			count = this.pstatement.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}

	ArrayList<GoodsBean> showBasket(GoodsBean gb) {
		ArrayList<GoodsBean> goodsList = new ArrayList<GoodsBean>();
		String query = "SELECT * FROM BL WHERE MID = ?";
		try {
			this.pstatement = this.connection.prepareStatement(query);
			this.pstatement.setNString(1, gb.getmId());

			this.rs = this.pstatement.executeQuery();
			while (rs.next()) {
				GoodsBean goods = new GoodsBean();
				goods.setmId(rs.getNString("MID"));
				goods.setmName(rs.getNString("MNAME"));
				goods.setGoCode(rs.getNString("GOCODE"));
				goods.setGoName(rs.getNString("GONAME"));
				goods.setGoPrice(rs.getInt("GOPRICE"));
				goods.setGoQty(rs.getInt("QTY"));
				goods.setGoImage(rs.getNString("GOIMAGE"));
				goods.setSeCode(rs.getNString("SECODE"));
				goods.setSeName(rs.getNString("SENAME"));
				goodsList.add(goods);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return goodsList;

	}

	public AuthBean getCustomerInfo(String sessionId) { // 회원정보
		String query = "SELECT MM_NAME,MM_PHONE FROM MM WHERE MM_ID=?";
		AuthBean auth = new AuthBean();
		try {
			this.pstatement = this.connection.prepareStatement(query);
			this.pstatement.setNString(1, sessionId);
			this.rs = this.pstatement.executeQuery();
			while (rs.next()) {
				auth.setmName(rs.getNString("MM_NAME"));
				auth.setPhone(rs.getNString("MM_PHONE"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return auth;
	}

	public GoodsBean getOrderList(GoodsBean gb) { // 주문서 만들기~
		String query = "SELECT MID, GOCODE, GONAME, SECODE, SENAME, QUANTITY, PRICE, IMAGE  FROM ORDER_LIST WHERE MID = ? AND GOCODE = ? AND SECODE = ?";
		GoodsBean goods = new GoodsBean();
		try {
			this.pstatement = this.connection.prepareStatement(query);
			this.pstatement.setNString(1, gb.getmId());
			this.pstatement.setNString(2, gb.getGoCode());
			this.pstatement.setNString(3, gb.getSeCode());
			this.rs = this.pstatement.executeQuery();
			while (rs.next()) {
				goods.setmId(rs.getNString("MID"));
				goods.setGoCode(rs.getNString("GOCODE"));
				goods.setGoName(rs.getNString("GONAME"));
				goods.setSeCode(rs.getNString("SECODE"));
				goods.setSeName(rs.getNString("SENAME"));
				goods.setGoQty(rs.getInt("QUANTITY"));
				goods.setGoPrice(rs.getInt("PRICE"));
				goods.setGoImage(rs.getNString("IMAGE"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return goods;
	}

	public int insOrder(GoodsBean gb) {
		int count = 0;
		// String query = "INSERT INTO \"OR\"(OR_MMID, OR_DATE, OR_STATE)
		// VALUES(?,TO_DATE(?, 'YYYYMMDDHH24MISS'),?)"; // 상태 I로 고정해놓고 나중에 수정.
		String query = "INSERT INTO \"OR\"(OR_MMID, OR_DATE, OR_STATE) VALUES(?,DEFAULT,?)";
		try {
			this.pstatement = this.connection.prepareStatement(query);
			this.pstatement.setNString(1, gb.getmId());
			// this.pstatement.setNString(2, gb.getOrDate());
			this.pstatement.setNString(2, gb.getOrderState());
			count = this.pstatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public String searchOrderDate(GoodsBean gb) {
		String orderDate = null;
		String query = "SELECT TO_CHAR(MAX(OR_DATE), \'YYYYMMDDHH24MISS\') AS ORDATE FROM \"OR\" WHERE OR_MMID=?";

		try {
			this.pstatement = this.connection.prepareStatement(query);
			this.pstatement.setNString(1, gb.getmId());
			this.rs = this.pstatement.executeQuery();
			while (rs.next()) {
				orderDate = rs.getNString("ORDATE");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return orderDate;

	}

	public int insOrderDetail(GoodsBean gb) {
		int count = 0;
		String query = "INSERT INTO OD(OD_ORMMID, OD_ORDATE, OD_SAGOCODE, OD_QUANTITY, OD_STATE, OD_SASECODE) VALUES(?, TO_DATE(?, 'YYYYMMDDHH24MISS'), ?, ?, ?, ?)";
		try {
			this.pstatement = this.connection.prepareStatement(query);
			this.pstatement.setNString(1, gb.getmId());
			this.pstatement.setNString(2, gb.getOrderDate()); // 날짜
			this.pstatement.setNString(3, gb.getGoCode()); //
			this.pstatement.setInt(4, gb.getGoQty());
			this.pstatement.setString(5, gb.getOrderState());
			this.pstatement.setString(6, gb.getSeCode());

			count = this.pstatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return count;
	}
	
	public int setOrderState(GoodsBean gb) {
		int count = 0;
		this.pstatement = null;
		String query = "UPDATE \"OR\" SET OR_STATE = ? WHERE OR_MMID = ? AND OR_DATE = TO_DATE(?, 'YYYYMMDDHH24MISS')";
		try {
			this.pstatement = this.connection.prepareStatement(query);
			this.pstatement.setNString(1, gb.getOrderState());
			this.pstatement.setNString(2, gb.getmId());
			this.pstatement.setNString(3, gb.getOrderDate());
			count = this.pstatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;

	}
	public int setOrderDetailState(GoodsBean gb) {
		int count = 0;
		this.pstatement = null;
		String query = "UPDATE OD SET OD_STATE = ? WHERE OD_ORMMID = ? AND OD_ORDATE = TO_DATE(?, 'YYYYMMDDHH24MISS') AND OD_SAGOCODE = ?";
		try {
			this.pstatement = this.connection.prepareStatement(query);
			this.pstatement.setNString(1, gb.getOrderDetailState());
			this.pstatement.setNString(2, gb.getmId());
			this.pstatement.setNString(3, gb.getOrderDate());
			this.pstatement.setNString(4, gb.getGoCode());
			count = this.pstatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public int deleteBasket(GoodsBean gb) {
		int count = 0;
		this.pstatement = null;
		String query = "DELETE FROM BA WHERE BA_MMID=?";
		try {
			this.pstatement = this.connection.prepareStatement(query);
			this.pstatement.setNString(1, gb.getmId());
			count = this.pstatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
}
