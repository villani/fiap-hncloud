package br.com.qrdapio.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A ItemPedido.
 */
@Entity
@Table(name = "item_pedido")
public class ItemPedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Min(value = 1)
    @Column(name = "quantidade")
    private Integer quantidade;

    @JsonIgnoreProperties(value = { "cardapio" }, allowSetters = true)
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private ItemCardapio item;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "itemPedidos", "restaurante" }, allowSetters = true)
    private Pedido pedido;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ItemPedido id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getQuantidade() {
        return this.quantidade;
    }

    public ItemPedido quantidade(Integer quantidade) {
        this.quantidade = quantidade;
        return this;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public ItemCardapio getItem() {
        return this.item;
    }

    public ItemPedido item(ItemCardapio itemCardapio) {
        this.setItem(itemCardapio);
        return this;
    }

    public void setItem(ItemCardapio itemCardapio) {
        this.item = itemCardapio;
    }

    public Pedido getPedido() {
        return this.pedido;
    }

    public ItemPedido pedido(Pedido pedido) {
        this.setPedido(pedido);
        return this;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemPedido)) {
            return false;
        }
        return id != null && id.equals(((ItemPedido) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemPedido{" +
            "id=" + getId() +
            ", quantidade=" + getQuantidade() +
            "}";
    }
}
