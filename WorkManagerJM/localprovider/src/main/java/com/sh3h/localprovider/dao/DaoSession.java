package com.sh3h.localprovider.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.sh3h.localprovider.entity.History;
import com.sh3h.localprovider.entity.Material;
import com.sh3h.localprovider.entity.MultiMedia;
import com.sh3h.localprovider.entity.Person;
import com.sh3h.localprovider.entity.Task;
import com.sh3h.localprovider.entity.Temporary;
import com.sh3h.localprovider.entity.Word;

import com.sh3h.localprovider.dao.HistoryDao;
import com.sh3h.localprovider.dao.MaterialDao;
import com.sh3h.localprovider.dao.MultiMediaDao;
import com.sh3h.localprovider.dao.PersonDao;
import com.sh3h.localprovider.dao.TaskDao;
import com.sh3h.localprovider.dao.TemporaryDao;
import com.sh3h.localprovider.dao.WordDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig historyDaoConfig;
    private final DaoConfig materialDaoConfig;
    private final DaoConfig multiMediaDaoConfig;
    private final DaoConfig personDaoConfig;
    private final DaoConfig taskDaoConfig;
    private final DaoConfig temporaryDaoConfig;
    private final DaoConfig wordDaoConfig;

    private final HistoryDao historyDao;
    private final MaterialDao materialDao;
    private final MultiMediaDao multiMediaDao;
    private final PersonDao personDao;
    private final TaskDao taskDao;
    private final TemporaryDao temporaryDao;
    private final WordDao wordDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        historyDaoConfig = daoConfigMap.get(HistoryDao.class).clone();
        historyDaoConfig.initIdentityScope(type);

        materialDaoConfig = daoConfigMap.get(MaterialDao.class).clone();
        materialDaoConfig.initIdentityScope(type);

        multiMediaDaoConfig = daoConfigMap.get(MultiMediaDao.class).clone();
        multiMediaDaoConfig.initIdentityScope(type);

        personDaoConfig = daoConfigMap.get(PersonDao.class).clone();
        personDaoConfig.initIdentityScope(type);

        taskDaoConfig = daoConfigMap.get(TaskDao.class).clone();
        taskDaoConfig.initIdentityScope(type);

        temporaryDaoConfig = daoConfigMap.get(TemporaryDao.class).clone();
        temporaryDaoConfig.initIdentityScope(type);

        wordDaoConfig = daoConfigMap.get(WordDao.class).clone();
        wordDaoConfig.initIdentityScope(type);

        historyDao = new HistoryDao(historyDaoConfig, this);
        materialDao = new MaterialDao(materialDaoConfig, this);
        multiMediaDao = new MultiMediaDao(multiMediaDaoConfig, this);
        personDao = new PersonDao(personDaoConfig, this);
        taskDao = new TaskDao(taskDaoConfig, this);
        temporaryDao = new TemporaryDao(temporaryDaoConfig, this);
        wordDao = new WordDao(wordDaoConfig, this);

        registerDao(History.class, historyDao);
        registerDao(Material.class, materialDao);
        registerDao(MultiMedia.class, multiMediaDao);
        registerDao(Person.class, personDao);
        registerDao(Task.class, taskDao);
        registerDao(Temporary.class, temporaryDao);
        registerDao(Word.class, wordDao);
    }
    
    public void clear() {
        historyDaoConfig.clearIdentityScope();
        materialDaoConfig.clearIdentityScope();
        multiMediaDaoConfig.clearIdentityScope();
        personDaoConfig.clearIdentityScope();
        taskDaoConfig.clearIdentityScope();
        temporaryDaoConfig.clearIdentityScope();
        wordDaoConfig.clearIdentityScope();
    }

    public HistoryDao getHistoryDao() {
        return historyDao;
    }

    public MaterialDao getMaterialDao() {
        return materialDao;
    }

    public MultiMediaDao getMultiMediaDao() {
        return multiMediaDao;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public TaskDao getTaskDao() {
        return taskDao;
    }

    public TemporaryDao getTemporaryDao() {
        return temporaryDao;
    }

    public WordDao getWordDao() {
        return wordDao;
    }

}