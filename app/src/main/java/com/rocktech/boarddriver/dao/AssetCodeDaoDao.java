package com.rocktech.boarddriver.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.rocktech.boarddriver.bean.AssetCodeDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ASSET_CODE_DAO".
*/
public class AssetCodeDaoDao extends AbstractDao<AssetCodeDao, Void> {

    public static final String TABLENAME = "ASSET_CODE_DAO";

    /**
     * Properties of entity AssetCodeDao.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property AssetCode = new Property(0, String.class, "assetCode", false, "ASSET_CODE");
        public final static Property BoxId = new Property(1, String.class, "boxId", false, "BOX_ID");
        public final static Property AssetCodeTitle = new Property(2, String.class, "assetCodeTitle", false, "ASSET_CODE_TITLE");
        public final static Property ProductModel = new Property(3, String.class, "productModel", false, "PRODUCT_MODEL");
    }


    public AssetCodeDaoDao(DaoConfig config) {
        super(config);
    }
    
    public AssetCodeDaoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ASSET_CODE_DAO\" (" + //
                "\"ASSET_CODE\" TEXT," + // 0: assetCode
                "\"BOX_ID\" TEXT," + // 1: boxId
                "\"ASSET_CODE_TITLE\" TEXT," + // 2: assetCodeTitle
                "\"PRODUCT_MODEL\" TEXT);"); // 3: productModel
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ASSET_CODE_DAO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, AssetCodeDao entity) {
        stmt.clearBindings();
 
        String assetCode = entity.getAssetCode();
        if (assetCode != null) {
            stmt.bindString(1, assetCode);
        }
 
        String boxId = entity.getBoxId();
        if (boxId != null) {
            stmt.bindString(2, boxId);
        }
 
        String assetCodeTitle = entity.getAssetCodeTitle();
        if (assetCodeTitle != null) {
            stmt.bindString(3, assetCodeTitle);
        }
 
        String productModel = entity.getProductModel();
        if (productModel != null) {
            stmt.bindString(4, productModel);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, AssetCodeDao entity) {
        stmt.clearBindings();
 
        String assetCode = entity.getAssetCode();
        if (assetCode != null) {
            stmt.bindString(1, assetCode);
        }
 
        String boxId = entity.getBoxId();
        if (boxId != null) {
            stmt.bindString(2, boxId);
        }
 
        String assetCodeTitle = entity.getAssetCodeTitle();
        if (assetCodeTitle != null) {
            stmt.bindString(3, assetCodeTitle);
        }
 
        String productModel = entity.getProductModel();
        if (productModel != null) {
            stmt.bindString(4, productModel);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public AssetCodeDao readEntity(Cursor cursor, int offset) {
        AssetCodeDao entity = new AssetCodeDao( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // assetCode
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // boxId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // assetCodeTitle
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // productModel
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, AssetCodeDao entity, int offset) {
        entity.setAssetCode(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setBoxId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setAssetCodeTitle(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setProductModel(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(AssetCodeDao entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(AssetCodeDao entity) {
        return null;
    }

    @Override
    public boolean hasKey(AssetCodeDao entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}