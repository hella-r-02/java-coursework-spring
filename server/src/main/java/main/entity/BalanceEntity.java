package main.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Table(name = "balance")
@Entity
public class BalanceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "create_date", nullable = false)
    @NotNull
    private LocalDateTime create_date;

    @Column(name = "debit", nullable = false)
    @NotNull
    private Double debit;
    @Column(name = "credit", nullable = false)
    @NotNull
    private Double credit;
    @Column(name = "amount", nullable = false)
    @NotNull
    private Double amount;
    @JsonManagedReference
    @OneToMany(mappedBy = "balance", cascade = CascadeType.ALL, targetEntity = OperationEntity.class)
    private List<OperationEntity> operations;

    public BalanceEntity() {
    }

    public BalanceEntity(LocalDateTime create_date, Double debit, Double credit, Double amount) {
        this.create_date = create_date;
        this.debit = debit;
        this.credit = credit;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime get_create_date() {
        return create_date;
    }

    public Double getDebit() {
        return debit;
    }

    public Double getCredit() {
        return credit;
    }

    public Double getAmount() {
        return amount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void set_create_date(LocalDateTime create_date) {
        this.create_date = create_date;
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Balance{" +
                "id=" + id +
                ", create_date=" + create_date +
                ", debit=" + debit +
                ", credit=" + credit +
                ", amount=" + amount +
                '}';
    }
}
