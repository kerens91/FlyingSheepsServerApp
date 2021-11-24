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
@Table(name="card_decoration")
public class DecoreEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="frame")
	private String frameImg;
	
	@Column(name="background")
	private String backImg;
	
	@Column(name="image")
	private String img;
	
	@Column(name="text_color")
	private int txtCol;
	
	@Column(name="points_image")
	private String pointsImg;
	
	@ManyToOne
	@JoinColumn(name = "card_id")
	private CardEntity card;

	public DecoreEntity() {}
	
	public DecoreEntity(String frameImg, String backImg, String img, int txtCol, String pointsImg,
			CardEntity card) {
		this.frameImg = frameImg;
		this.backImg = backImg;
		this.img = img;
		this.txtCol = txtCol;
		this.pointsImg = pointsImg;
		this.card = card;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFrameImg() {
		return frameImg;
	}

	public void setFrameImg(String frameImg) {
		this.frameImg = frameImg;
	}

	public String getBackImg() {
		return backImg;
	}

	public void setBackImg(String backImg) {
		this.backImg = backImg;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getTxtCol() {
		return txtCol;
	}

	public void setTxtCol(int txtCol) {
		this.txtCol = txtCol;
	}

	public String getPointsImg() {
		return pointsImg;
	}

	public void setPointsImg(String pointsImg) {
		this.pointsImg = pointsImg;
	}

	public CardEntity getCard() {
		return card;
	}

	public void setCard(CardEntity card) {
		this.card = card;
	}
	
	
	
	
}
