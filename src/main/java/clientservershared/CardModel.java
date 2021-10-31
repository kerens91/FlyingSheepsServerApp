package clientservershared;

public class CardModel {
	private int id;
    private String name;
    private String img;
    private int txtColor;
    private String frame;
    private String back;
    private String points;

    public CardModel(int id, String name, String img, int txtColor, String frame, String back, String points) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.txtColor = txtColor;
        this.back = back;
        this.frame = frame;
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImg() {
        return img;
    }

	public int getTxtColor() {
		return txtColor;
	}

	public String getFrame() {
		return frame;
	}

	public String getBack() {
		return back;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}
    
}
