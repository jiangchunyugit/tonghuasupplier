package cn.thinkfree.service.platform.designer.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author xusonghui
 * 证件类型对象
 */
public class CardTypeVo {

    @ApiModelProperty("证件类型编码")
    private int cardCode;
    @ApiModelProperty("证件类型名称")
    private String cardName;

    public CardTypeVo(int cardCode, String cardName) {
        this.cardCode = cardCode;
        this.cardName = cardName;
    }

    public CardTypeVo() {
    }

    public int getCardCode() {
        return cardCode;
    }

    public void setCardCode(int cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }
}
