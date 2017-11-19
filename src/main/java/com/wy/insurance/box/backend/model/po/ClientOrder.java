package com.wy.insurance.box.backend.model.po;

import java.util.Date;

/**
 * C端用户订单
 * @author daobin<wdb@winbaoxian.com>
 * @date 2017/11/17.
 */
public class ClientOrder {
    /** ID */
    private String id;
    /** 序列号 */
    private String serialNo;
    /** 产品ID */
    private Long productId;
    /** APP用户ID */
    private Long userId;
    /** 保单号 */
    private String policySn;
    /** sales_insure_policy表的主键uuid */
    private String policyUuid;
    /**  */
    private String policySnVerify;
    /** 保单生效时间 */
    private Date policyStartDatetime;
    /** 保单失效时间 */
    private Date policyEndDatetime;
    /** 投保人姓名 */
    private String holderName;
    /** 投保人手机号 */
    private String holderMobile;
    /** 投保人证件类型 */
    private String holderCardType;
    /** 投保人证件号码 */
    private String holderCardValue;
    /** 投保人id */
    private String holderId;
    /** 投保人生日 */
    private Date holderBirthday;
    /** 投保人邮箱 */
    private String holderEmail;
    /** 投保人性别 1-男,2-女 */
    private Integer holderSex;
    /** 被保人姓名 */
    private String insuredName;
    /** 被保人邮箱 */
    private String insuredEmail;
    /** 被保人手机号 */
    private String insuredMobile;
    /** 被保人证件类型 */
    private String insuredCardType;
    /** 被保人证件号码 */
    private String insuredCardValue;
    /** 被保人ID */
    private String insuredId;
    /** 性别 1-男,2-女 */
    private Integer insuredSex;
    /** 被保人与投保人关系 */
    private Integer insuredRelation;
    /** 被保人生日 */
    private Date insuredBirthday;
    /** 投保成功时间 */
    private Date insureSuccessDatetime;
    /** 订单已撤单 */
    private Integer statusCode;
    /** 保单类型 */
    private String policyType;
    /** 赠券uuid */
    private String couponuid;
    /** 投保失败信息 */
    private String failMsg;
    /** 保额 */
    private Double policyAmount;
    /** 保费 */
    private Double policyPremium;
    /** 佣金 */
    private Double commission;
    /** 推广奖励 */
    private Double pushReward;
    /**  */
    private Integer source;
    /**  */
    private String sourceUuid;
    /** 否 */
    private Integer isDeleted;
    /** 否 */
    private Integer isEnable;
    /** 创建时间 */
    private Date createDatetime;
    /** 修改时间 */
    private Date updateDatetime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPolicySn() {
        return policySn;
    }

    public void setPolicySn(String policySn) {
        this.policySn = policySn;
    }

    public String getPolicyUuid() {
        return policyUuid;
    }

    public void setPolicyUuid(String policyUuid) {
        this.policyUuid = policyUuid;
    }

    public String getPolicySnVerify() {
        return policySnVerify;
    }

    public void setPolicySnVerify(String policySnVerify) {
        this.policySnVerify = policySnVerify;
    }

    public Date getPolicyStartDatetime() {
        return policyStartDatetime;
    }

    public void setPolicyStartDatetime(Date policyStartDatetime) {
        this.policyStartDatetime = policyStartDatetime;
    }

    public Date getPolicyEndDatetime() {
        return policyEndDatetime;
    }

    public void setPolicyEndDatetime(Date policyEndDatetime) {
        this.policyEndDatetime = policyEndDatetime;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getHolderMobile() {
        return holderMobile;
    }

    public void setHolderMobile(String holderMobile) {
        this.holderMobile = holderMobile;
    }

    public String getHolderCardType() {
        return holderCardType;
    }

    public void setHolderCardType(String holderCardType) {
        this.holderCardType = holderCardType;
    }

    public String getHolderCardValue() {
        return holderCardValue;
    }

    public void setHolderCardValue(String holderCardValue) {
        this.holderCardValue = holderCardValue;
    }

    public String getHolderId() {
        return holderId;
    }

    public void setHolderId(String holderId) {
        this.holderId = holderId;
    }

    public Date getHolderBirthday() {
        return holderBirthday;
    }

    public void setHolderBirthday(Date holderBirthday) {
        this.holderBirthday = holderBirthday;
    }

    public String getHolderEmail() {
        return holderEmail;
    }

    public void setHolderEmail(String holderEmail) {
        this.holderEmail = holderEmail;
    }

    public Integer getHolderSex() {
        return holderSex;
    }

    public void setHolderSex(Integer holderSex) {
        this.holderSex = holderSex;
    }

    public String getInsuredName() {
        return insuredName;
    }

    public void setInsuredName(String insuredName) {
        this.insuredName = insuredName;
    }

    public String getInsuredEmail() {
        return insuredEmail;
    }

    public void setInsuredEmail(String insuredEmail) {
        this.insuredEmail = insuredEmail;
    }

    public String getInsuredMobile() {
        return insuredMobile;
    }

    public void setInsuredMobile(String insuredMobile) {
        this.insuredMobile = insuredMobile;
    }

    public String getInsuredCardType() {
        return insuredCardType;
    }

    public void setInsuredCardType(String insuredCardType) {
        this.insuredCardType = insuredCardType;
    }

    public String getInsuredCardValue() {
        return insuredCardValue;
    }

    public void setInsuredCardValue(String insuredCardValue) {
        this.insuredCardValue = insuredCardValue;
    }

    public String getInsuredId() {
        return insuredId;
    }

    public void setInsuredId(String insuredId) {
        this.insuredId = insuredId;
    }

    public Integer getInsuredSex() {
        return insuredSex;
    }

    public void setInsuredSex(Integer insuredSex) {
        this.insuredSex = insuredSex;
    }

    public Integer getInsuredRelation() {
        return insuredRelation;
    }

    public void setInsuredRelation(Integer insuredRelation) {
        this.insuredRelation = insuredRelation;
    }

    public Date getInsuredBirthday() {
        return insuredBirthday;
    }

    public void setInsuredBirthday(Date insuredBirthday) {
        this.insuredBirthday = insuredBirthday;
    }

    public Date getInsureSuccessDatetime() {
        return insureSuccessDatetime;
    }

    public void setInsureSuccessDatetime(Date insureSuccessDatetime) {
        this.insureSuccessDatetime = insureSuccessDatetime;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    public String getCouponuid() {
        return couponuid;
    }

    public void setCouponuid(String couponuid) {
        this.couponuid = couponuid;
    }

    public String getFailMsg() {
        return failMsg;
    }

    public void setFailMsg(String failMsg) {
        this.failMsg = failMsg;
    }

    public Double getPolicyAmount() {
        return policyAmount;
    }

    public void setPolicyAmount(Double policyAmount) {
        this.policyAmount = policyAmount;
    }

    public Double getPolicyPremium() {
        return policyPremium;
    }

    public void setPolicyPremium(Double policyPremium) {
        this.policyPremium = policyPremium;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Double getPushReward() {
        return pushReward;
    }

    public void setPushReward(Double pushReward) {
        this.pushReward = pushReward;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getSourceUuid() {
        return sourceUuid;
    }

    public void setSourceUuid(String sourceUuid) {
        this.sourceUuid = sourceUuid;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Integer isEnable) {
        this.isEnable = isEnable;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
}
