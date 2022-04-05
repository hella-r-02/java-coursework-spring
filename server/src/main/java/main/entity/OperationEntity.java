package main.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
@Table(name = "operations")
public class OperationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;


    @JsonBackReference
    @ManyToOne(optional = false, targetEntity = ArticleEntity.class)
    @JoinColumn(name = "article_id", nullable = false)
    private ArticleEntity article;


    @Column(name = "debit", nullable = false)
    @NotNull
    private Double debit;
    @Column(name = "credit", nullable = false)
    @NotNull
    private Double credit;
    @Column(name = "create_date", nullable = false)
    @NotNull
    private LocalDateTime create_date;

    @JsonBackReference
    @ManyToOne(optional = false, targetEntity = BalanceEntity.class)
    @JoinColumn(name = "balance_id", nullable = false)
    private BalanceEntity balance;


    public OperationEntity(ArticleEntity article, Double debit, Double credit, LocalDateTime create_date, BalanceEntity balance) {
        this.article = article;
        this.debit = debit;
        this.credit = credit;
        this.create_date = create_date;
        this.balance = balance;
    }

    public OperationEntity(Long id, ArticleEntity article, Double debit, Double credit, LocalDateTime create_date, BalanceEntity balance) {
        this.id = id;
        this.article = article;
        this.debit = debit;
        this.credit = credit;
        this.create_date = create_date;
        this.balance = balance;
    }

    public OperationEntity() {
    }

    public Long getId() {
        return id;
    }

    public ArticleEntity getArticle() {
        return article;
    }

    public Double getDebit() {
        return debit;
    }

    public Double getCredit() {
        return credit;
    }

    public LocalDateTime getCreate_date() {
        return create_date;
    }

    public BalanceEntity getBalance() {
        return balance;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setArticle(ArticleEntity article) {
        this.article = article;
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public void setCreate_date(LocalDateTime create_date) {
        this.create_date = create_date;
    }

    public void setBalance(BalanceEntity balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "id=" + id +
                ", article=" + article +
                ", debit=" + debit +
                ", credit=" + credit +
                ", create_date=" + create_date +
                ", balance=" + balance +
                '}';
    }
}
