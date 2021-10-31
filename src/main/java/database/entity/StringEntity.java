package database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="cardsMessages")
public class StringEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="MESSAGEID")
	private int id;
	
	@Column(name="TYPE")
	private int type;
	
	@Column(name="STATE")
	private int state;
	
	@Column(name="DESTINATION")
	private int dest;
	
	@Column(name="PRESTRING")
	private int pre;
	
	@Column(name="STRING")
	private String string;
	
	@Column(name="POSTSTRING")
	private int post;
	
	@ManyToOne
	@JoinColumn(name = "CARDID")
	private CardEntity card;

	public StringEntity() {}

	public StringEntity(int type, int state, int dest, int pre, String string, int post, CardEntity card) {
		this.type = type;
		this.state = state;
		this.dest = dest;
		this.pre = pre;
		this.string = string;
		this.post = post;
		this.card = card;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getDest() {
		return dest;
	}

	public void setDest(int dest) {
		this.dest = dest;
	}

	public int getPre() {
		return pre;
	}

	public void setPre(int pre) {
		this.pre = pre;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public int getPost() {
		return post;
	}

	public void setPost(int post) {
		this.post = post;
	}

	public CardEntity getCard() {
		return card;
	}

	public void setCard(CardEntity card) {
		this.card = card;
	}

	@Override
	public String toString() {
		return "StringEntity [id=" + id + ", type=" + type + ", state=" + state + ", dest=" + dest + ", pre=" + pre
				+ ", string=" + string + ", post=" + post + ", card=" + card.getId() + "]";
	}

	public void printString() {
		System.out.printf("StringEntity [id=%-10d, type=%-10d, state=%-10d, dest=%-10d, pre=%-10d, string=%-20s, post=%-10d, card=%-10d]%n", id, type, state, dest, pre, string, post, card.getId());
	}
	
	
}
