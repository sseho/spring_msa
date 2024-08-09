package com.beyond.order_system.ordering.repository;

import com.beyond.order_system.ordering.domain.Ordering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Ordering, Long> {
    List<Ordering> findByMemberEmail(String email);
}
