package com.umc.withme.repository;

import com.umc.withme.domain.Meet;
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

    // 모임 전체 리스트 조회
    // 1. 카테고리 2. 내동네/온동네 3. 제목으로 검색
    @Override
    public List<Meet> takeAll(MeetSearch meetSearch) {
        // 모임과 주소 정보를 조회한다.
        String jpql = "select distinct m from Meet m" +
                " join MeetAddress ma on m.id = ma.meet.id" +
                " join Address a on a.id = ma.address.id";
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
}
