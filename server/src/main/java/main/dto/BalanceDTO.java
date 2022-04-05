package main.dto;

public class BalanceDTO {
    private Long id;
    private String create_date;
    private Double debit;
    private Double credit;
    private Double amount;

    public BalanceDTO() {
    }

    public BalanceDTO(Long id, String create_date, Double debit, Double credit, Double amount) {
        this.id = id;
        this.create_date = create_date;
        this.debit = debit;
        this.credit = credit;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public Double getDebit() {
        return debit;
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
