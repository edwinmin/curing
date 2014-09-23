package com.edwin.curing.helper;

import com.edwin.curing.constant.IbatisConstant;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * sql语句辅助类
 * 
 * @author jinming.wu
 * @date 2014-8-22
 */
public class SqlHelper {

    /**
     * 生成count sql
     * 
     * @param sql
     * @return
     */
    public static String gernerateCountSql(String sql) {

        Preconditions.checkArgument(!Strings.isNullOrEmpty(sql), "Can not find sql: " + sql);

        String _sql = sql.trim().toUpperCase();

        if (_sql.endsWith(";")) {
            _sql = _sql.substring(0, _sql.length());
        }

        _sql = _sql.replaceAll(IbatisConstant.DESC, "");
        _sql = _sql.replaceAll(IbatisConstant.ASC, "");
        _sql = _sql.replaceAll("\r\n", "\\s");
        _sql = _sql.replaceAll("\r\n", "\\s");
        _sql = _sql.replaceAll("\\s+", "\\s");
        _sql = _sql.replaceAll("\\s+,\\s+", ",");

        StringBuffer countSql = new StringBuffer();

        int fromIndex = _sql.indexOf(IbatisConstant.FROM);

        countSql.append(IbatisConstant.SELECT);
        countSql.append("COUNT(*) AS _COUNT_");
        countSql.append(IbatisConstant.FROM);
        countSql.append(_sql.substring(fromIndex));

        _sql = countSql.toString();

        _sql = elininateToken(_sql, IbatisConstant.GROUP_BY);
        _sql = elininateToken(_sql, IbatisConstant.ORDER_BY);

        return _sql;
    }

    private static String elininateToken(final String sql, final String token) {

        String _sql = sql.trim();

        int tokenIndex = _sql.indexOf(token);

        if (tokenIndex == -1) {
            return _sql;
        }

        StringBuffer countSql = new StringBuffer();

        countSql.append(_sql.subSequence(0, tokenIndex));

        _sql = _sql.substring(token.length()).trim();

        int continueIndex = -1;

        for (int i = 0; i < _sql.length(); i++) {
            char c = _sql.charAt(i);

            if (c == ' ') {
                continueIndex = i + 1;
                break;
            }
        }

        if (continueIndex != -1 && continueIndex < _sql.length()) {
            countSql.append(_sql.substring(continueIndex));
        }

        return _sql;
    }
}
