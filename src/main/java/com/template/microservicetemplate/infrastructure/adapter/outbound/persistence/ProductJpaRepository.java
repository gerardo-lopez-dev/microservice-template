package com.template.microservicetemplate.infrastructure.adapter.outbound.persistence;

/**
 * Código de ejemplo — eliminar o reemplazar al implementar el dominio real.
 */

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, UUID> {
}
