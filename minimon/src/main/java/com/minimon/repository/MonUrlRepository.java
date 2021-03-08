package com.minimon.repository;

import com.minimon.entity.MonUrl;
import com.minimon.entity.QMonUrl;
import com.minimon.enums.UseStatusEnum;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MonUrlRepository extends JpaRepository<MonUrl, Integer> {
    List<MonUrl> findByMonitoringUseYn(UseStatusEnum monitoringUseYn);
}

interface MonUrlSupport {
    List<MonUrl> findByText(String text);

    @Transactional
    class MonUrlSupportImpl extends QueryDslRepositorySupportWrapper implements MonUrlSupport {
        QMonUrl qMonUrl = QMonUrl.monUrl;

        public MonUrlSupportImpl() {
            super(MonUrl.class);
        }

        @Override
        public List<MonUrl> findByText(String text) {
            JPQLQuery query = from(qMonUrl)
                    .where(qMonUrl.title.like(text))
                    .where(qMonUrl.url.like(text));
            return query.fetch();
        }
    }
}