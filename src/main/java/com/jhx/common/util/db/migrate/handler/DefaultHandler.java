package com.jhx.common.util.db.migrate.handler;

import com.jhx.common.util.db.migrate.annotation.IndexColumn;
import com.jhx.common.util.db.migrate.annotation.Migrate;
import com.jhx.common.util.db.migrate.core.DataType;
import com.jhx.common.util.db.migrate.core.MigrateCore;
import com.jhx.common.util.db.migrate.model.ColumnModel;
import com.jhx.common.util.db.migrate.model.TableModel;
import com.jhx.common.util.validate.Display;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * 默认的处理
 *
 * @author t.ch
 */
public class DefaultHandler {

    private static void EntityHandler(Annotation annotation, TableModel tableModel) {
        Entity entity = (Entity) annotation;
        if (StringUtils.isNotBlank(entity.name())) {
            tableModel.TABLE_NAME = entity.name();
        }
    }

    private static void TableHandler(Annotation annotation, TableModel tableModel) {
        Table table = (Table) annotation;
        if (StringUtils.isNotBlank(table.name())) {
            tableModel.TABLE_NAME = table.name();
        }
        UniqueConstraint[] uniqueConstraints = table.uniqueConstraints();
        for (UniqueConstraint uniqueConstraint : uniqueConstraints) {
            tableModel.addKey(null, true, uniqueConstraint.columnNames());
        }
        Index[] indexes = table.indexes();
        for (Index index : indexes) {
            if (index.unique()) {
                tableModel.addKey(null, true, index.columnList().split(","));
            } else {
                tableModel.addKey(null, false, index.columnList().split(","));
            }
        }
    }

    private static void MigrateHandler(Annotation annotation, TableModel tableModel) {
        Migrate migrate = (Migrate) annotation;
        if (StringUtils.isNotBlank(migrate.oldName())) {
            tableModel.OLD_TABLE_NAME = migrate.oldName();
        }
    }

    private static void ColumnHandler(Annotation annotation, ColumnModel columnModel) {
        Column column = (Column) annotation;
        if (StringUtils.isNotBlank(column.name())) {
            columnModel.COLUMN_NAME = column.name();
        }
        if (StringUtils.isNotBlank(column.columnDefinition())) {
            columnModel.COLUMN_TYPE = column.columnDefinition();
        }
        columnModel.IS_NULLABLE = column.nullable() ? "YES" : "NO";
        columnModel.CHARACTER_MAXIMUM_LENGTH = column.length(); // 字符类型 长度
        columnModel.NUMERIC_PRECISION = column.precision(); // 数字类型 长度
        columnModel.NUMERIC_SCALE = column.scale(); // 浮点型的小数位数
        if(column.unique()){
            columnModel.getTableModel().addKey(null, true,columnModel.COLUMN_NAME);
        }
    }

    private static void IdHandler(Annotation annotation, ColumnModel columnModel) {
        //Id id = (Id) annotation;
        columnModel.IS_NULLABLE = "NO";
        columnModel.getTableModel().addPrimaryKey(columnModel.COLUMN_NAME);
    }

    private static void GeneratedValueHandler(Annotation annotation, ColumnModel columnModel) {
        GeneratedValue generatedValue = (GeneratedValue) annotation;
        columnModel.EXTRA = generatedValue.strategy() == GenerationType.AUTO ? "AUTO_INCREMENT" : "";
    }


    private static void NotNullHandler(Annotation annotation, ColumnModel columnModel) {
        columnModel.IS_NULLABLE = "NO";
    }

    private static void SizeHandler(Annotation annotation, ColumnModel columnModel) {
        Size size = (Size) annotation;
        columnModel.CHARACTER_MAXIMUM_LENGTH = size.max();
    }

    private static void DisplayHandler(Annotation annotation, ColumnModel columnModel) {
        Display display = (Display) annotation;
        columnModel.COLUMN_COMMENT = display.value();
    }

    private static void MigrateHandler(Annotation annotation, ColumnModel columnModel) {
        Migrate migrate = (Migrate) annotation;
        if (StringUtils.isNotBlank(migrate.oldName())) {
            columnModel.OLD_COLUMN_NAME = migrate.oldName();
        }
    }

    private static void IndexColumnHandler(Annotation annotation, ColumnModel columnModel) {
        IndexColumn indexColumn = (IndexColumn) annotation;
        columnModel.getTableModel().addKey(indexColumn.value(), indexColumn.unique(), columnModel.COLUMN_NAME);
    }

    // Digits 只对数字有效
    private static void DigitsHandler(Annotation annotation, ColumnModel columnModel){
        Digits digits = (Digits) annotation;
        columnModel.NUMERIC_PRECISION = digits.integer() + digits.fraction(); // 数字类型 长度
        columnModel.NUMERIC_SCALE = digits.fraction(); // 浮点型的小数位数
    }

    // Length 只有max有效 只对字符串有效
    private static void LengthHandler(Annotation annotation, ColumnModel columnModel){
        Length length = (Length) annotation;
        columnModel.CHARACTER_MAXIMUM_LENGTH = length.max(); // 字符类型 长度
    }

    // Range 只有max有效 只对字符串有效(因为数字没法限制最大值和最小值,只能限制位数)
    private static void RangeHandler(Annotation annotation, ColumnModel columnModel){
        Range range = (Range) annotation;
        columnModel.CHARACTER_MAXIMUM_LENGTH = range.max(); // 字符类型 长度
    }

    public static void register() {
        // 表
        MigrateCore.addTableAnnotationHandler(Entity.class, DefaultHandler::EntityHandler);
        MigrateCore.addTableAnnotationHandler(Table.class, DefaultHandler::TableHandler);
        MigrateCore.addTableAnnotationHandler(Migrate.class, DefaultHandler::MigrateHandler);
        // 字段
        MigrateCore.addColumnAnnotationHandler(Column.class, DefaultHandler::ColumnHandler);
        MigrateCore.addColumnAnnotationHandler(Id.class, DefaultHandler::IdHandler);
        MigrateCore.addColumnAnnotationHandler(IndexColumn.class, DefaultHandler::IndexColumnHandler);
        MigrateCore.addColumnAnnotationHandler(GeneratedValue.class, DefaultHandler::GeneratedValueHandler);
        MigrateCore.addColumnAnnotationHandler(Size.class, DefaultHandler::SizeHandler);
        MigrateCore.addColumnAnnotationHandler(Display.class, DefaultHandler::DisplayHandler);
        MigrateCore.addColumnAnnotationHandler(Migrate.class, DefaultHandler::MigrateHandler);
        MigrateCore.addColumnAnnotationHandler(NotNull.class, DefaultHandler::NotNullHandler);
        MigrateCore.addColumnAnnotationHandler(NotBlank.class, DefaultHandler::NotNullHandler);
        MigrateCore.addColumnAnnotationHandler(NotEmpty.class, DefaultHandler::NotNullHandler);
        MigrateCore.addColumnAnnotationHandler(Length.class, DefaultHandler::LengthHandler);
        MigrateCore.addColumnAnnotationHandler(Range.class, DefaultHandler::RangeHandler);
        MigrateCore.addColumnAnnotationHandler(Digits.class, DefaultHandler::DigitsHandler);

    }

    public static void defaultDataTypeMap() {
        MigrateCore.addDataType(byte.class, DataType.Tinyint);
        MigrateCore.addDataType(short.class, DataType.Smallint);
        MigrateCore.addDataType(int.class, DataType.Int);
        MigrateCore.addDataType(long.class, DataType.Bigint);
        MigrateCore.addDataType(float.class, DataType.Float);
        MigrateCore.addDataType(double.class, DataType.Double);
        MigrateCore.addDataType(boolean.class, DataType.Bit);
        MigrateCore.addDataType(char.class, DataType.Char);
        MigrateCore.addDataType(Byte.class, DataType.Tinyint);
        MigrateCore.addDataType(Short.class, DataType.Smallint);
        MigrateCore.addDataType(Integer.class, DataType.Int);
        MigrateCore.addDataType(Long.class, DataType.Bigint);
        MigrateCore.addDataType(Float.class, DataType.Float);
        MigrateCore.addDataType(Double.class, DataType.Double);
        MigrateCore.addDataType(Boolean.class, DataType.Bit);
        MigrateCore.addDataType(Character.class, DataType.Char);
        MigrateCore.addDataType(BigInteger.class, DataType.Bigint);
        MigrateCore.addDataType(BigDecimal.class, DataType.Decimal);
        MigrateCore.addDataType(String.class, DataType.Varchar);
        MigrateCore.addDataType(Date.class, DataType.Datetime);
        MigrateCore.addDataType(java.sql.Date.class, DataType.Datetime);
        MigrateCore.addDataType(LocalDateTime.class, DataType.Datetime);
        MigrateCore.addDataType(Timestamp.class, DataType.Timestamp);
        MigrateCore.addDataType(LocalDate.class, DataType.Date);
        MigrateCore.addDataType(LocalTime.class, DataType.Time);
        MigrateCore.addDataType(byte[].class, DataType.Blob);
    }
}
