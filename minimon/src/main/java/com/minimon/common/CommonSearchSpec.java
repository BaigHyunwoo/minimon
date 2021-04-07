package com.minimon.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Setter
@Getter
@ApiModel(value = "공통 리스트 조회(조건, 페이징, 정렬) 정보")
public class CommonSearchSpec {
    @ApiModelProperty(value = "페이지 위치 (기본값 1)")
    private int index = 1;

    @ApiModelProperty(value = "페이지 내 개수 (기본값 20)")
    private int size = 100;

    @ApiModelProperty(value = "조건 키")
    private String[] keys;

    @ApiModelProperty(value = "조건 종류(like, equal, greaterOrEqual, lessOrEqual, greater, less, in, between)")
    private String[] types;

    @ApiModelProperty(value = "조건 값(between, in = XXX, AAA)")
    private String[] values;

    @ApiModelProperty(value = "정렬 키")
    private String sortKey;

    @ApiModelProperty(value = "정렬 종류(ASC, DESC)")
    private String sortType;

    public void setSize(int size) {
        this.size = size > 0 ? size : this.size;
    }

    public void setIndex(int index) {
        this.index = index > 0 ? index : this.index;
    }


    public String getValueByKey(String input) {
        for (int i = 0; i < this.keys.length; i++) {
            if (input.equals(keys[i])) return values[i];
        }
        return null;
    }

    public String getTypeByKey(String input) {
        for (int i = 0; i < this.keys.length; i++) {
            if (input.equals(keys[i])) return types[i];
        }
        return null;
    }

    public PageRequest pageRequest() {
        int page = this.index - 1;
        if (page < 0) page = 0;

        if (this.sortType.equals(Sort.Direction.DESC.name()) == true) {
            return PageRequest.of(page, this.size, Sort.Direction.DESC, this.sortKey);
        } else if (this.sortType.equals(Sort.Direction.ASC.name()) == true) {
            return PageRequest.of(page, this.size, Sort.Direction.ASC, this.sortKey);
        }
        return PageRequest.of(page, this.size);
    }

    public Specification searchSpecs() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            setPredicates(root, cb, predicates);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public boolean checkDateType(String value) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        boolean check = true;

        try {
            LocalDateTime.parse(value, formatter);
        } catch (Exception e) {
            check = false;
        }
        return check;
    }

    public LocalDateTime getDate(String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(value, formatter);
    }

    public void setPredicates(Root root, CriteriaBuilder cb, List<Predicate> predicates) {
        Optional.ofNullable(this.types).ifPresent(types -> {
            for (int i = 0; i < types.length; i++) {
                try {
                    String type = this.types[i];
                    String key = this.keys[i];
                    String value = this.values[i];
                    String likeValueString = "%" + value + "%";
                    switch (type) {
                        case "like":
                            predicates.add(cb.like(root.get(key), likeValueString));
                            break;
                        case "equal":
                            predicates.add(cb.equal(root.get(key), value));
                            break;
                        case "notEqual":
                            predicates.add(cb.notEqual(root.get(key), value));
                            break;
                        case "greaterOrEqual":
                            if (checkDateType(value) == true)
                                predicates.add(cb.greaterThanOrEqualTo(root.get(key), getDate(value)));
                            else predicates.add(cb.greaterThanOrEqualTo(root.get(key), value));
                            break;
                        case "lessOrEqual":
                            if (checkDateType(value) == true)
                                predicates.add(cb.lessThanOrEqualTo(root.get(key), getDate(value)));
                            else predicates.add(cb.lessThanOrEqualTo(root.get(key), value));
                            break;
                        case "greater":
                            if (checkDateType(value) == true)
                                predicates.add(cb.greaterThan(root.get(key), getDate(value)));
                            else predicates.add(cb.greaterThan(root.get(key), value));
                            break;
                        case "less":
                            if (checkDateType(value) == true)
                                predicates.add(cb.lessThan(root.get(key), getDate(value)));
                            else predicates.add(cb.lessThan(root.get(key), value));
                            break;
                        case "in":
                            Object[] args = value.split(",");
                            predicates.add(root.get(key).in(args));
                            break;
                        case "between":
                            if (checkDateType(value.split(",")[0]) == true) {
                                predicates.add(
                                        cb.between(root.get(key),
                                                getDate(value.split(",")[0]),
                                                getDate(value.split(",")[1])));
                            } else {
                                predicates.add(
                                        cb.between(root.get(key),
                                                Integer.parseInt(value.split(",")[0]),
                                                Integer.parseInt(value.split(",")[1])));
                            }
                            break;
                    }
                } catch (Exception e) {
                    log.debug("CommonSearchSpec create query error");
                }
            }
        });
    }
}
