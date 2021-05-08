package br.com.qrdapio.domain;

import br.com.qrdapio.domain.enumeration.FormaPagamento;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Pedido.
 */
@Entity
@Table(name = "pedido")
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", nullable = false)
    private FormaPagamento formaPagamento;

    @NotNull
    @Column(name = "data_hora", nullable = false)
    private ZonedDateTime dataHora;

    @Column(name = "senha", unique = true)
    private Integer senha;

    @OneToMany(mappedBy = "pedido")
    @JsonIgnoreProperties(value = { "item", "pedido" }, allowSetters = true)
    private Set<ItemPedido> itemPedidos = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "cardapios", "pedidos" }, allowSetters = true)
    private Restaurante restaurante;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pedido id(Long id) {
        this.id = id;
        return this;
    }

    public FormaPagamento getFormaPagamento() {
        return this.formaPagamento;
    }

    public Pedido formaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
        return this;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public ZonedDateTime getDataHora() {
        return this.dataHora;
    }

    public Pedido dataHora(ZonedDateTime dataHora) {
        this.dataHora = dataHora;
        return this;
    }

    public void setDataHora(ZonedDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Integer getSenha() {
        return this.senha;
    }

    public Pedido senha(Integer senha) {
        this.senha = senha;
        return this;
    }

    public void setSenha(Integer senha) {
        this.senha = senha;
    }

    public Set<ItemPedido> getItemPedidos() {
        return this.itemPedidos;
    }

    public Pedido itemPedidos(Set<ItemPedido> itemPedidos) {
        this.setItemPedidos(itemPedidos);
        return this;
    }

    public Pedido addItemPedido(ItemPedido itemPedido) {
        this.itemPedidos.add(itemPedido);
        itemPedido.setPedido(this);
        return this;
    }

    public Pedido removeItemPedido(ItemPedido itemPedido) {
        this.itemPedidos.remove(itemPedido);
        itemPedido.setPedido(null);
        return this;
    }

    public void setItemPedidos(Set<ItemPedido> itemPedidos) {
        if (this.itemPedidos != null) {
            this.itemPedidos.forEach(i -> i.setPedido(null));
        }
        if (itemPedidos != null) {
            itemPedidos.forEach(i -> i.setPedido(this));
        }
        this.itemPedidos = itemPedidos;
    }

    public Restaurante getRestaurante() {
        return this.restaurante;
    }

    public Pedido restaurante(Restaurante restaurante) {
        this.setRestaurante(restaurante);
        return this;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pedido)) {
            return false;
        }
        return id != null && id.equals(((Pedido) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pedido{" +
            "id=" + getId() +
            ", formaPagamento='" + getFormaPagamento() + "'" +
            ", dataHora='" + getDataHora() + "'" +
            ", senha=" + getSenha() +
            "}";
    }
}
