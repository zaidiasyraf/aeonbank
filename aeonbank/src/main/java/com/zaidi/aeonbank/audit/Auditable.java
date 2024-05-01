package com.zaidi.aeonbank.audit;

import java.time.LocalDateTime;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable {

    @CreatedDate
    protected LocalDateTime createdOn;

    @LastModifiedDate
    protected LocalDateTime updatedOn;

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(final LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(final LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

}
