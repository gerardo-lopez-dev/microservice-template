package com.template.microservicetemplate.infrastructure.adapter.outbound.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringProductRepository extends JpaRepository<ProductJpaEntity, UUID> {
}
