package com.gukbit.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import com.gukbit.domain.Course;
import com.gukbit.domain.Rate;
import com.gukbit.repository.RateRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;

@Service("WordAnalysisService")
public class WordAnalysisService implements IWordAnalysisService {
    private final RateService rateService;
    private final CourseService courseService;
    private Komoran nlp = null;

    @Autowired
    public WordAnalysisService(RateService rateService,CourseService courseService) {
        this.nlp = new Komoran(DEFAULT_MODEL.LIGHT); // 학습데이터 경량화 버전( 웹 서비스에 적합합니다. )
        this.rateService = rateService;
        this.courseService = courseService;
    }

    //자연어 처리 - 형태소 분석기인 Komoran를 메모리에 올리기 위해 WordAnalysisService 클래스 내 전역 변수로 설정합니다.


    @Override
    public List<String> doWordNouns(String text) throws Exception {
        List<String> rList= new ArrayList<String>();

        //분석할 문장에 대해 정제(쓸데없는 특수문자 제거)
        String replace_text = text.replace("[^가-힣a-zA-Z0-9", " ");


        //분석할 문장의 앞, 뒤에 존재할 수 있는 필요없는 공백 제거
        String trim_text = replace_text.trim();


        //형태소 분석 시작
        KomoranResult analyzeResultList = this.nlp.analyze(trim_text);

        //형태소 분석 결과 중 명사만 가져오기
        rList = analyzeResultList.getNouns();

        return rList;
    }

    @Override
    public Map<String, Integer> doWordCount(List<String> pList) throws Exception {


        if (pList ==null) {
            pList = new ArrayList<String>();
        }

        //단어 빈도수(사과, 3) 결과를 저장하기 위해 Map객체 생성합니다.
        Map<String, Integer> rMap = new HashMap<>();

        //List에 존재하는 중복되는 단어들의 중복제거를 위해 set 데이터타입에 데이터를 저장합니다.
        //rSet 변수는 중복된 데이터가 저장되지 않기 떄문에 중복되지 않은 단어만 저장하고 나머지는 자동 삭제합니다.
        Set<String> rSet = new HashSet<>(pList);

        //중복이 제거된 단어 모음에 빈도수를 구하기 위해 반복문을 사용합니다.
        Iterator<String> it = rSet.iterator();

        while(it.hasNext()) {
            //중복 제거된 단어
            String word = it.next();

            //단어가 중복 저장되어 있는 pList로부터 단어의 빈도수 가져오기
            int frequency = Collections.frequency(pList, word);

            rMap.put(word, frequency);
        }


        return rMap;
    }

    @Override
    public List<Map<String,Integer>> doWordAnalysis(String academyId) throws Exception {
        //장점Map
        Map<String, Integer> aMap = new HashMap<>();
        //단점Map
        Map<String, Integer> dMap = new HashMap<>();
        List<String> aList = new ArrayList<>(); //장점
        List<String> dList = new ArrayList<>(); //단점

        List<Course> list = courseService.getCourseListByAcademyId(academyId);

        List<String> listId = new ArrayList<>();
        for (Course course : list) {
            listId.add(course.getCid());
        }

        //리뷰 가져오기
        List<Rate> rateList = rateService.findAllRateByCourseIdIn(listId);
        List<String> advantageList = new ArrayList<>(); //장점 리스트
        List<String> disadvantageList = new ArrayList<>(); //단점 리스트

        for (Rate rate : rateList) {
            advantageList.add(rate.getAdvantage());
            disadvantageList.add(rate.getDisadvantage());
        }


        //문장의 명사를 추출하기 위한 형태소 분석 실행
        for (String a : advantageList) {
            aList.addAll(this.doWordNouns(a));
        }

        for (String d : disadvantageList) {
            dList.addAll(this.doWordNouns(d));
        }

        //추출된 명사 모음(리스트)의 명사 단어별 빈도수 계산
        aMap.putAll(this.doWordCount(aList));
        dMap.putAll(this.doWordCount(dList));

        List<Map<String,Integer>> resultList = new ArrayList<>();
        resultList.add(aMap);
        resultList.add(dMap);

        return resultList;
    }

}
