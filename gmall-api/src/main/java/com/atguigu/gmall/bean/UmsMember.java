package com.atguigu.gmall.bean;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class UmsMember {
    /**
     * `id` bigint(20) NOT NULL AUTO_INCREMENT,
     * `member_level_id` bigint(20) DEFAULT NULL,
     * `username` varchar(64) DEFAULT NULL COMMENT '鐢ㄦ埛鍚?',
     * `password` varchar(64) DEFAULT NULL COMMENT '瀵嗙爜',
     * `nickname` varchar(64) DEFAULT NULL COMMENT '鏄电О',
     * `phone` varchar(64) DEFAULT NULL COMMENT '鎵嬫満鍙风爜',
     * `status` int(1) DEFAULT NULL COMMENT '甯愬彿鍚?敤鐘舵??:0->绂佺敤锛?1->鍚?敤',
     * `create_time` datetime DEFAULT NULL COMMENT '娉ㄥ唽鏃堕棿',
     * `icon` varchar(500) DEFAULT NULL COMMENT '澶村儚',
     * `gender` int(1) DEFAULT NULL COMMENT '鎬у埆锛?0->鏈?煡锛?1->鐢凤紱2->濂?',
     * `birthday` date DEFAULT NULL COMMENT '鐢熸棩',
     * `city` varchar(64) DEFAULT NULL COMMENT '鎵?鍋氬煄甯?',
     * `job` varchar(100) DEFAULT NULL COMMENT '鑱屼笟',
     * `personalized_signature` varchar(200) DEFAULT NULL COMMENT '涓??х?鍚?',
     * `source_type` int(1) DEFAULT NULL COMMENT '鐢ㄦ埛鏉ユ簮',
     * `integration` int(11) DEFAULT NULL COMMENT '绉?垎',
     * `growth` int(11) DEFAULT NULL COMMENT '鎴愰暱鍊?',
     * `luckey_count` int(11) DEFAULT NULL COMMENT '鍓╀綑鎶藉?娆℃暟',
     * `history_integration` int(11) DEFAULT NULL COMMENT '鍘嗗彶绉?垎鏁伴噺',
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String memberLevelId;
    private String username;
    private String password;
    private String nickname;
    private String phone;
    private int status;
    private Date createTime;
    private String icon;
    private int gender;
    private Date birthday;
    private String city;
    private String job;
    private String personalizedSignature;
    private int sourceType;
    private int integration;
    private int growth;
    private int luckeyCount;
    private int historyIntegration;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberLevelId() {
        return memberLevelId;
    }

    public void setMemberLevelId(String memberLevelId) {
        this.memberLevelId = memberLevelId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getPersonalizedSignature() {
        return personalizedSignature;
    }

    public void setPersonalizedSignature(String personalizedSignature) {
        this.personalizedSignature = personalizedSignature;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public int getIntegration() {
        return integration;
    }

    public void setIntegration(int integration) {
        this.integration = integration;
    }

    public int getGrowth() {
        return growth;
    }

    public void setGrowth(int growth) {
        this.growth = growth;
    }

    public int getLuckeyCount() {
        return luckeyCount;
    }

    public void setLuckeyCount(int luckeyCount) {
        this.luckeyCount = luckeyCount;
    }

    public int getHistoryIntegration() {
        return historyIntegration;
    }

    public void setHistoryIntegration(int historyIntegration) {
        this.historyIntegration = historyIntegration;
    }
}
