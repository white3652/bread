package com.bread.app.controller;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bread.app.dao.AdminMemDAO;
import com.bread.app.dao.AdminStoreDAO;
import com.bread.app.dao.CartDAO;
import com.bread.app.dao.MemberDAO;
import com.bread.app.dao.SearchDAO;
import com.bread.app.vo.BreadVO;
import com.bread.app.vo.CartVO;
import com.bread.app.vo.MemberVO;
import com.bread.service.member.MemberService;
import com.bread.service.sms.SmsService;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RestController
@AllArgsConstructor
@Slf4j
public class AjaxController {
	//private SmsService smsService;
    private MemberDAO memberDAO;
    private SearchDAO searchDAO;
    private AdminMemDAO adminMemDAO;
    private AdminStoreDAO adminStoreDAO;
    private CartDAO cartDAO;
    private MemberService mUpdate;

    
    //////////////////// 중복 검사 ////////////////////
    
    //아이디 중복검사 처리 요청
    @PostMapping("/member/checkIdProcess.do")
    public String checkIdProcess(String member_id) throws SQLException {
        String result="0";//중복 아이디가 없는 경우 결과값
        
        System.out.println("member_id:"+member_id);
        
        int checkId = memberDAO.checkId(member_id);
        if(checkId != 0) { //중복일경우
            result = "1";//중복 아이디가 있는 경우 결과값
        }

        return result;
    }
  //닉네임 중복검사
    @PostMapping("/member/checkNicknameProcess.do")
    public String checkNicknameProcess(String member_nickname) throws SQLException {
        String result="0";//중복 닉네임이 없는 경우
        
        int checkNickname = memberDAO.checkNickname(member_nickname);
        if(checkNickname != 0) { //중복일경우
            result = "1";
        }

        return result;
    }
    
    //전화번호 중복검사
    @PostMapping("/member/checkPhoneNumberProcess.do")
    public String checkPhoneNumbrtProcess(String member_phone) throws SQLException {
        String result="0";//중복이 없는 경우
        
        int checkPhoneNumber = memberDAO.checkPhoneNumber(member_phone);
        if(checkPhoneNumber != 0) { //중복일경우
            result = "1";
        }

        return result;
    }
    
    //닉네임 중복검사
    @PostMapping("/member/checkUpdateNicknameProcess.do")
    public int checkNicknameProcess(String member_nickname, int member_idx) throws SQLException {
    	int result=0;//중복이 없는 경우, 확인한 전화번호가 현재 사용중인 전화번호인 경우
        
        int idx = memberDAO.checkUpdateNickname(member_nickname);
        if(idx != member_idx) { //확인한 닉네임의 member_idx가 회원정보를 변경하는 회원의 member_idx가 아닌 경우
            result = 1;
        }

        return result;
    }
    
    //전화번호 중복검사
    @PostMapping("/member/checkUpdatePhoneNumberProcess.do")
    public int checkPhoneNumbrtProcess(String member_phone, int member_idx) throws SQLException {
        int result=0;//중복이 없는 경우, 확인한 전화번호가 현재 사용중인 전화번호인 경우
        
        int idx = memberDAO.checkUpdatePhoneNumber(member_phone);
        if(idx != member_idx) { //확인한 전화번호의 member_idx가 회원정보를 변경하는 회원의 member_idx가 아닌 경우
            result = 1;
        }

        return result;
    }
    
    //회원가입 alert
    @PostMapping("/member/joinProcess.do")
    public ResponseEntity<?> joinProcessAjax(@ModelAttribute MemberVO memberVO) { //ResponseEntity<?> ajax성공이나 오류 즉, 상태를 response출력해주기위해 사용됨
        Map<String, Object> response = new HashMap<>();
        try {

            int result = memberDAO.join(memberVO);
            if (result == 1) {
                // 회원가입 성공
                response.put("status", "success");
                response.put("message", "회원가입이 성공적으로 완료되었습니다.\n로그인하여 서비스를 이용해주세요.");
                return ResponseEntity.ok(response);
            } else {
                // 회원가입 실패
                response.put("status", "fail");
                response.put("message", "회원가입에 실패했습니다. 입력 정보를 다시 확인해 주세요."); 
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            // 예외 발생시 상세한 에러 로그 출력
            log.error("회원가입 도중 예외 발생", e);

            response.put("status", "error");
            response.put("message", "서버 오류로 인해 처리할 수 없습니다. 관리자에게 문의해 주세요."); 
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/member/updateProcess.do")
    public ResponseEntity<?> updateProcessAjax(@ModelAttribute MemberVO memberVO, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            MemberVO updatedMember = mUpdate.update(memberVO, request);
            if (updatedMember != null) {
                // 회원 정보 업데이트 성공
                HttpSession session = request.getSession();
                session.removeAttribute("member");
                session.setAttribute("member", updatedMember);

                response.put("status", "success");
                response.put("message", "회원 정보가 성공적으로 업데이트되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                // 회원 정보 업데이트 실패
                response.put("status", "fail");
                response.put("message", "업데이트에 실패했습니다. 입력 정보를 다시 확인해 주세요.");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            // 예외 발생 시
            log.error("회원 정보 업데이트 도중 예외 발생", e);
            response.put("status", "error");
            response.put("message", "서버 오류로 인해 처리할 수 없습니다. 관리자에게 문의해 주세요.");
            return ResponseEntity.badRequest().body(response);
        }
    }
    //////////////////// 장바구니 페이지 ////////////////////
    
    @PostMapping("/search/cartAdd.do")
    public BreadVO cartAdd(int bread_idx, int member_idx) throws SQLException{
    	BreadVO breadVO = null;
    	if(searchDAO.checkCart(bread_idx, member_idx) != 0) {
    		
    	}else {
    		breadVO = searchDAO.getBread(bread_idx);
    	}
    	
    	return breadVO;
    }
    
    @PostMapping("/**/payProcess.do")
    public String payProcess(String order_idx, int amount, int member_idx) throws SQLException {
        
        // 회원 정보 조회
        MemberVO member = memberDAO.getMember(member_idx);
        String memberPhone = member.getMember_phone(); // 회원 전화번호 가져오기

        List<CartVO> cartList = cartDAO.getCarts(member_idx);
        
        cartDAO.addOrder(order_idx, amount, member_idx);

        for(int i=0; i<cartList.size(); i++) {
            int bread_idx = cartList.get(i).getBread_idx();
            int bakery_idx = cartList.get(i).getBakery_idx();
            int bread_count = cartList.get(i).getBread_count();
            int cart_idx = cartList.get(i).getCart_idx();
            
            cartDAO.addItem(bread_idx, bakery_idx, order_idx, bread_count);
            cartDAO.updateStock(bread_count, bread_idx);
            cartDAO.deleteCart(cart_idx);
        }
        
        // SMS 발송 로직 추가
//        String message = String.format("주문이 성공적으로 처리되었습니다. 주문 번호: %s, 금액: %d", order_idx, amount);
//        smsService.sendSms(memberPhone, message); // 수정된 부분: member_phone 대신 조회한 회원의 전화번호 사용

        return "OK";
    }
    
    @PostMapping("/**/deleteCart.do")
    public int deleteCart(int cart_idx) {
    	int result = 0;
    	
    	result = cartDAO.deleteCart(cart_idx);
    	
    	return result;
    }
    
    @PostMapping("/**/updateCount.do")
    public void updateCount(int cart_idx, int bread_count) {
    	cartDAO.updateCount(cart_idx, bread_count);
    }
    
    
    //////////////////// 관리자 페이지 ////////////////////

    @PostMapping("/admin/updateMem.do")
    public MemberVO updateMem(MemberVO memberVO) throws SQLException {
        return adminMemDAO.updateMem(memberVO);
    }

    @PostMapping("/admin/deleteMem.do")
    public int deleteMem(int member_idx) throws SQLException {
        return adminMemDAO.deleteMem(member_idx);
    }
    
    @PostMapping("/admin/deleteStore.do")
    public int deleteStore(int bakery_idx) throws SQLException {
        return adminStoreDAO.deleteStore(bakery_idx);
    }
}
