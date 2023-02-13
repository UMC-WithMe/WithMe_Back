package com.umc.withme.repository;

import com.umc.withme.domain.Meet;
import com.umc.withme.dto.meet.MeetRecordSearch;
import com.umc.withme.dto.meet.MeetSearch;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class CustomizedMeetRepositoryImpl implements CustomizedMeetRepository {
    @PersistenceContext
    EntityManager em;

    /**
     * 모임 전체 리스트 조회
     *
     * @param meetSearch 모임 리스트 필터 조건
     *                   1. 카테고리 2. 내동네/온동네 3.제목 4.모임진행상태
     * @return 조건에 해당하는 모임 엔티티 리스트
     */
    @Override
    public List<Meet> searchMeets(MeetSearch meetSearch) {
        // 모임과 주소 정보를 조회한다.
        String jpql = "select distinct m from Meet m" +
                " join MeetAddress ma on m.id = ma.meet.id" +
                " join Address a on a.id = ma.address.id" +
                " join MeetMember mm on m.id = mm.meet.id";
        boolean isFirstCondition = true;

        // 카테고리 검색 (전체가 아닌 경우)
        if (meetSearch.getMeetCategory() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.category = :category";
        }

        // 동네 검색 (내동네 설정을 한경우)
        if (meetSearch.getAdderess() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " a.sido = :sido and a.sgg = :sgg";
        }

        // 제목 검색
        if (meetSearch.getTitle() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.title LIKE CONCAT('%', :title, '%')";
        }

        // 쿼리 생성
        TypedQuery<Meet> query = em.createQuery(jpql, Meet.class)
                .setMaxResults(100);

        // 쿼리 파라미터 설정
        if (meetSearch.getMeetCategory() != null)
            query = query.setParameter("category", meetSearch.getMeetCategory());
        if (meetSearch.getAdderess() != null) {
            query = query.setParameter("sido", meetSearch.getAdderess().getSido());
            query = query.setParameter("sgg", meetSearch.getAdderess().getSgg());
        }
        if (meetSearch.getTitle() != null)
            query = query.setParameter("title", meetSearch.getTitle());

        return query.getResultList();
    }

    @Override
    public List<Meet> searchMeetRecords(MeetRecordSearch meetRecordSearch) {
        // 모임과 주소 정보를 조회한다.
        String jpql = "select distinct m from Meet m" +
                " join MeetAddress ma on m.id = ma.meet.id" +
                " join Address a on a.id = ma.address.id" +
                " join MeetMember mm on m.id = mm.meet.id";
        boolean isFirstCondition = true;


        // 모임 진행상태 검색 (PROGRESS, COMPLETE)
        if (meetRecordSearch.getMeetStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.meetStatus = :meetStatus";
        }

        // 모임 멤버 검색 (모임기록을 조회하고자 하는 사용자 id)
        if (meetRecordSearch.getMemberId() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " mm.member.id = :memberId";
        }

        // 쿼리 생성
        TypedQuery<Meet> query = em.createQuery(jpql, Meet.class)
                .setMaxResults(100);

        // 쿼리 파라미터 설정
        if (meetRecordSearch.getMeetStatus() != null)
            query = query.setParameter("meetStatus", meetRecordSearch.getMeetStatus());
        if (meetRecordSearch.getMemberId() != null)
            query = query.setParameter("memberId", meetRecordSearch.getMemberId());

        return query.getResultList();
    }
}
