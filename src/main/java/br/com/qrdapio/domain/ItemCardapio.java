package br.com.qrdapio.domain;

import br.com.qrdapio.domain.enumeration.Categoria;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A ItemCardapio.
 */
@Entity
@Table(name = "item_cardapio")
public class ItemCardapio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false)
    private Categoria categoria;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "valor", precision = 21, scale = 2)
    private BigDecimal valor;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "itemCardapios", "restaurante" }, allowSetters = true)
    private Cardapio cardapio;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ItemCardapio id(Long id) {
        this.id = id;
        return this;
    }

    public Categoria getCategoria() {
        return this.categoria;
    }

    public ItemCardapio categoria(Categoria categoria) {
        this.categoria = categoria;
        return this;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getNome() {
        return this.nome;
    }

    public ItemCardapio nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public ItemCardapio descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return this.valor;
    }

    public ItemCardapio valor(BigDecimal valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Cardapio getCardapio() {
        return this.cardapio;
    }

    public ItemCardapio cardapio(Cardapio cardapio) {
        this.setCardapio(cardapio);
        return this;
    }

    public void setCardapio(Cardapio cardapio) {
        this.cardapio = cardapio;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemCardapio)) {
            return false;
        }
        return id != null && id.equals(((ItemCardapio) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemCardapio{" +
            "id=" + getId() +
            ", categoria='" + getCategoria() + "'" +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", valor=" + getValor() +
            "}";
    }
}
