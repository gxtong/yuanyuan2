package com.jhx.common.util.dist.trade.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jhx.common.util.StrUtil;
import com.jhx.common.util.db.DbConstant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.socket.WebSocketSession;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author qzh
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class ModelInBase implements Serializable {
    protected static final long serialVersionUID = 1L;

    @Size(max = 200)
    private String srData;

    @Range(min = 0, max = Integer.MAX_VALUE)
    private int userId;

    @Size(max = DbConstant.IpLen)
    private String ip;

    @JsonIgnore
    private WebSocketSession session;

    @Override
    public String toString() {
        return StrUtil.toStr(this);
    }

    public ModelInBase(String srData, int userId, String ip) {
        this.srData = srData;
        this.userId = userId;
        this.ip = ip;
    }

    public ModelInBase(int userId, String ip) {
        this.userId = userId;
        this.ip = ip;
    }

}
