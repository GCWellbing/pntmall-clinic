package com.pntmall.clinic.etc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.pntmall.clinic.domain.Clinic;
import com.pntmall.common.utils.Utils;

/**
 * 어드민 로그인 정보 관리
 */
public class ClinicSession {
    private static String SESSION_KEY = "_PNTMALL_CLINIC_SESSION_" + Utils.getActiveProfile().toUpperCase();

	private HttpSession session;
	private Clinic clinic;

    /**
     * 생성자
     *
     * @param request HttpServletRequest
     */
    private ClinicSession(HttpServletRequest request) {
    	this.session = request.getSession();
    	clinic = (Clinic) session.getAttribute(ClinicSession.SESSION_KEY);
    }

    /**
     * 현재 로그인 정보 인스턴스를 반환.
     *
     * @param request HttpServletRequest
     * @return AdminSession Instance
     */
    public static ClinicSession getInstance(HttpServletRequest request) {
        return new ClinicSession(request);
    }

    /**
     * 현재 로그인 정보 인스턴스를 반환.
     *
     * @return 현재 로그인 정보 인스턴스
     */
    public static ClinicSession getInstance() {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        return getInstance(request);
    }

    /**
     * 로그인 수행
     *
     * @param admin 관리자 정보
     */
    public void login(Clinic clinic) {
    	session.setMaxInactiveInterval(30 * 60);
    	session.setAttribute(ClinicSession.SESSION_KEY, clinic);
    }

    /**
     * 로그아웃 수행
     */
    public void logout() {
        session.removeAttribute(ClinicSession.SESSION_KEY);
        if(session != null) {
        	session.invalidate();
        }
    }

    /**
     * 로그인 여부를 반환
     *
     * @return 로그인 여부
     */
    public boolean isLogin() {
        if(clinic != null && !"".equals(clinic.getMemId())) {
        	return true;
        } else {
        	return false;
        }
    }

    public Integer getMemNo() {
    	if(isLogin()) {
    		return clinic.getMemNo();
    	} else {
    		return 0;
    	}
    }

    /**
     * adminId
     * @return adminId
     */
    public String getMemId() {
    	if(isLogin()) {
    		return clinic.getMemId();
    	} else {
    		return "";
    	}
    }

    /**
     * name
     *
     * @return name
     */
    public String getName() {
    	if(isLogin()) {
    		return clinic.getName();
    	} else {
    		return "";
    	}
    }

    /**
     * name
     *
     * @return name
     */
    public Clinic getClinic() {
    	if(isLogin()) {
    		return clinic;
    	} else {
    		return null;
    	}
    }



}
