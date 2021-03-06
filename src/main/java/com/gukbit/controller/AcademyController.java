package com.gukbit.controller;

import com.gukbit.domain.*;
import com.gukbit.dto.AcademyDto;
import com.gukbit.etc.PopularSearchTerms;
import com.gukbit.etc.Today;
import com.gukbit.service.AcademyService;
import com.gukbit.service.BoardService;
import com.gukbit.service.CourseService;
import com.gukbit.service.RateService;
import com.gukbit.session.SessionConst;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/academy")
public class AcademyController {

    private final PopularSearchTerms popularSearchTerms;
    private final AcademyService academyService;
    private final RateService rateService;
    private final CourseService courseService;
    private final BoardService boardService;

    @Autowired
    public AcademyController(AcademyService academyService, RateService rateService, CourseService courseService, BoardService boardService, PopularSearchTerms popularSearchTerms) {
        this.popularSearchTerms = popularSearchTerms;
        this.academyService = academyService;
        this.rateService = rateService;
        this.courseService =  courseService;
        this.boardService = boardService;
    }

    @GetMapping("")
    public String academyBoard(@RequestParam(value = "academyCode") String academyCode,
                               Pageable pageable, Today today, Model model) {
        Page<Board> page = boardService.findAcademyBoardList(academyCode, pageable);
        model.addAttribute("boardList", page);
        model.addAttribute("Today", today);
        model.addAttribute("academyCode", academyCode);
        return "view/academy/academy-board";
    }

    //?????? ???
    @GetMapping({"/review", "/expected"})
    String academyMapping(@RequestParam("code") String code,
                          @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                          @Qualifier("reviewed") Pageable pageable1, @Qualifier("expected") Pageable pageable2,
                          Model model, HttpServletRequest request) {


        if(request.getRequestURL().toString().contains("/expected")){
            model.addAttribute("expectedSelect", true);
        }else{
            model.addAttribute("expectedSelect", false);
        }

        /* ?????? ???????????? ????????? ????????? */
        List<String> items = new ArrayList<>();
        items.add("?????????");
        items.add("????????????");
        items.add("?????? ??????");
        items.add("?????? ??? ??????");
        items.add("?????? ??? ??????");
        model.addAttribute("items", items);

        /* ?????? ?????? ?????? */

        Academy academyInfo = academyService.getAcademyInfo(code);
        model.addAttribute("academyInfo", academyInfo);

        /* ????????? Course/ reivewed Page ?????? ??????*/
        List<Course> courseList =  courseService.getCourseList(code);    
        double[] evalAll = academyService.reviewCourseAverage(courseList);
        int countAll = (int)evalAll[6];
        Page<Rate> page1 = academyService.reviewCoursePageList(courseList,pageable1);
        Page<Course> page2 = academyService.expectedCoursePageList(code, pageable2);
        model.addAttribute("reviewCoursePageList", page1);   
        model.addAttribute("expectedCoursePageList", page2);
        model.addAttribute("evalAll",evalAll);
        model.addAttribute("countAll",countAll);
        model.addAttribute("link1", "academy/review?code=" + code);
        model.addAttribute("link2", "academy/expected?code=" + code);



        //??????????????????
        try {
            List<Map<String,Integer>> list = academyService.analysis(code);
            model.addAttribute("advantageList", getJsonList(list.get(0), "??????"));
            model.addAttribute("disadvantageList", getJsonList(list.get(1),"??????"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        
        /* ????????? ?????? ?????? ?????? ?????? */
        try {
            String userId = loginUser.getUserId();
            AuthUserData authUserData = rateService.getAuthUserData(userId);
            model.addAttribute("authUserData", authUserData);

        } catch (NullPointerException e) {
            model.addAttribute("authUserData", null);
        }
        try {
            Boolean userRateCheck = rateService.findRateByUserId(loginUser.getUserId());
            model.addAttribute("userRateCheck", userRateCheck);
        } catch (NullPointerException e) {
            model.addAttribute("userRateCheck", false);

        }
        return "view/academy/academy";
    }
    @PostMapping("/review")
    @ResponseBody
    public Academy academyMapMapping(@RequestParam(value = "code") String code, Model model){
        return academyService.getAcademyInfo(code);
    }


    @GetMapping("/search")
    public String searchAcademy(@RequestParam(value = "keyword") String keyword, Model model, HttpServletRequest request, HttpServletResponse response) {
        boolean cookieHas = false;

        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                String name = cookie.getName();
                String value = cookie.getValue();
                if("popularKeyword".equals(name) && keyword.equals(value)) {
                    cookieHas = true;
                    break;
                }
            }
        }

        if(!cookieHas) {
            Cookie cookie = new Cookie("popularKeyword", keyword);
            cookie.setMaxAge(-1);
            response.addCookie(cookie);
            popularSearchTerms.insert(keyword);
        }
        List<AcademyDto> academyDtoList = academyService.searchAcademy(keyword);




        model.addAttribute("academyList", academyDtoList);
        model.addAttribute("keyword", keyword);
        return "/view/academy/search-academy";
    }

    //wordCloud ??????????????? ?????? ???????????? ?????? ?????? ??????
    @GetMapping("/wordCloud")
    @ResponseBody
    public List<JSONObject> wordCloud() {
        for (int i = 0; i < 15; i++)
            popularSearchTerms.insert("???????????????");
        for (int i = 0; i < 10; i++)
            popularSearchTerms.insert("??????");
        for (int i = 0; i < 20; i++)
            popularSearchTerms.insert("??????");
        return popularSearchTerms.getJson();
    }



    public List<JSONObject> getJsonList(Map<String,Integer> map, String mainText){
        List<JSONObject> list = new ArrayList<>();
        Integer maxValue = Collections.max(map.values());

        for (String s : map.keySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("text",s);
            jsonObject.put("weight",map.get(s));
            list.add(jsonObject);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text", mainText);
        jsonObject.put("weight", maxValue + 1);
        list.add(jsonObject);

        return list;
    }
}
