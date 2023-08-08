package com.db.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.db.dto.BoardDto;

public class BoardDao {
	Connection con = null;
	
	public BoardDao() {
		//driver연결
		try {
			 Class.forName("org.mariadb.jdbc.Driver");
		}catch(ClassNotFoundException e ) {
			e.printStackTrace();
		}
	}
	//전체 출력
	public List<BoardDto> selectAll(){
		try {
			con = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/testdb","zxc","1234"); //데이터베이스 명 잘쓰기
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Statement stmt = null;
		ResultSet rs = null;
		List<BoardDto> res = new ArrayList<BoardDto>();
		
		String sql = "SELECT * FROM BOARD";
		
		//sql쿼리 실행
		try {
			stmt = con.createStatement();
			
			//쿼리 실행 결과 rs에 저장
			rs = stmt.executeQuery(sql);
			
			//rs dto에 순서대로저장
			while(rs.next()) {
				BoardDto dto = new BoardDto(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDate(5));
				
				res.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
				con.close();
			} catch (SQLException e ) {
				e.printStackTrace();
			}
		}
		
		return res;
		
	}
	
	//게시글 선택 출력
	public BoardDto selectOne(int bd_no) {
		try {//db연결
			con = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/testdb","zxc","1234");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		PreparedStatement pstm = null;
		ResultSet rs = null;
		BoardDto res = null;
		
		String sql = "SELECT * FROM BOARD WHERE BD_NO=?";
		
		//sql 쿼리 실행
		try {
			pstm = con.prepareStatement(sql);
			pstm.setInt(1, bd_no);
			
			//쿼리 실행 결과 rs에 저장
			rs = pstm.executeQuery();
			
			while(rs.next()) {
				res = new BoardDto();
				res.setBd_no(rs.getInt(1));
				res.setBd_name(rs.getString(2));
				res.setBd_title(rs.getString(3));
				res.setBd_content(rs.getString(4));
				res.setBd_date(rs.getDate(5));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstm.close();
				con.close();
			} catch (SQLException e ) {
				e.printStackTrace();
			}
		}
		return res;
	}
	  //글 추가
	public int insert(BoardDto dto) {
		try {//db연결
			con = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/testdb","zxc","1234");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		PreparedStatement pstm = null;
		int res = 0;
		
		String sql = "INSERT INTO BOARD(BD_NAME, BD_TITLE, BD_CONTENT, BD_DATE) VALUES( ?, ?, ?, NOW())";
		
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, dto.getBd_name());
			pstm.setString(2, dto.getBd_title());
			pstm.setString(3, dto.getBd_content());
			
			res = pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstm.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return res;
	}  
	  //글 수정
	public int update(BoardDto dto) {
		try {//db연결
			con = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/testdb","zxc","1234");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		PreparedStatement pstm = null;
		int res = 0;
		
		String sql = " UPDATE BOARD SET BD_TITLE=?, BD_CONTENT=? WHERE BD_NO=? ";
		
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, dto.getBd_title());
			pstm.setString(2, dto.getBd_content());
			pstm.setInt(3, dto.getBd_no());
			
			res = pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstm.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return res;
	}
	  //글 삭제
	public int delete(int bd_no) {
		try {//db연결
			con = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/testdb","zxc","1234");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		PreparedStatement pstm = null;
		int res = 0;
		
		String sql = "DELETE FROM BOARD WHERE BD_NO=?; set @count = 0; update board set board.BD_NO= @count:=@count+1;";
				
		/*
		 * String sql1 = "alter table board auto_increment=1;"; String sql2 =
		 * "set @count = 0;"; String sql3 =
		 * "update board set BD_NO = @count:=@count+1;";
		 */
		 
		try {
			pstm = con.prepareStatement(sql);
			pstm.setInt(1, bd_no);
			
			res = pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstm.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return res;
	}
	 
}