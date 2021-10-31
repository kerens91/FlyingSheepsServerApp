package database.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="cards")
public class CardEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="CARDID")
	private int id;
	
	@Column(name="TYPE")
	private String type;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="VALUE")
	private int value;
	
	@Column(name="NUMMULT")
	private int mult;
	
	@Column(name="NUMADD")
	private int add;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "card")
	private List<StringEntity> cardStrings = new ArrayList<StringEntity>();

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "card")
	private DecoreEntity decore;

	
	public CardEntity() {}

	public CardEntity(String type, String name, int value, int mult, int add) {
		this.type = type;
		this.name = name;
		this.value = value;
		this.mult = mult;
		this.add = add;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getMult() {
		return mult;
	}

	public void setMult(int mult) {
		this.mult = mult;
	}

	public int getAdd() {
		return add;
	}

	public void setAdd(int add) {
		this.add = add;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

	public List<StringEntity> getCardStrings() {
		return cardStrings;
	}

	public void setCardStrings(List<StringEntity> cardStrings) {
		this.cardStrings = cardStrings;
	}

	public DecoreEntity getDecore() {
		return decore;
	}

	public void setDecore(DecoreEntity decore) {
		this.decore = decore;
	}

	@Override
	public String toString() {
		return "CardEntity [id=" + id + ", type=" + type + ", name=" + name + ", value=" + value
				+ ", mult=" + mult + ", add=" + add + ", cardStrings=" + cardStrings.toString() + "]";
	}

	public void printCard() {
		System.out.printf("CardEntity [id=%-10d, type=%-20s, name=%-20s, value=%-10d, mult=%-10d, add=%-10d]%n", id, type, name, value, mult, add);
		if (!cardStrings.isEmpty()) {
			System.out.printf("Card Strings:%n");
			for (StringEntity string : cardStrings) {
				string.printString();
			}
		}
		System.out.printf("%n");
	}

	
	
}
