package com.mariano.itunestopfreeapplications.data.source;

import com.mariano.itunestopfreeapplications.data.Application;
import com.mariano.itunestopfreeapplications.data.Category;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

public class RealmService {

    private final Realm mRealm;

    public RealmService(final Realm realm) {
        mRealm = realm;
    }

    public void closeRealm() {
        mRealm.close();
    }

    public RealmResults<Application> getAllApps() {
        return mRealm.where(Application.class).findAll();
    }

    public Application getApplication(final int id) {
        return mRealm.where(Application.class).equalTo("id", id).findFirst();
    }
    public Category getCategory(final int id) {
        return mRealm.where(Category.class).equalTo("id", id).findFirst();
    }

    public RealmResults<Application> getAllApps(String query) {
        return mRealm.where(Application.class).contains("title",query, Case.INSENSITIVE).findAll();
    }

    /*public void addBookAsync(final String title, final String author, final String isbn, final String publisher,
                             final OnTransactionCallback onTransactionCallback) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(final Realm realm) {
                Book book = realm.createObject(Book.class);
                book.setId(realm.allObjects(Book.class).size());
                book.setTitle(title);
                book.setAuthor(createOrGetAuthor(author, book, realm));
                book.setPublisher(createOrGetPublisher(publisher, book, realm));
                book.setIsbn(isbn);
            }
        }, new Realm.Transaction.Callback() {

            @Override
            public void onSuccess() {
                if (onTransactionCallback != null) {
                    onTransactionCallback.onRealmSuccess();
                }
            }

            @Override
            public void onError(final Exception e) {
                if (onTransactionCallback != null) {
                    onTransactionCallback.onRealmError(e);
                }
            }
        });
    }

    private Author createOrGetAuthor(final String author, final Book book, final Realm realm) {
        String[] authorName = splitAuthorName(author);
        Author foundAuthor = getAuthor(authorName, realm);
        if(foundAuthor == null) {
            foundAuthor = addAuthor(authorName, realm);
        }
        foundAuthor.getBooks().add(book);
        return foundAuthor;
    }

    private Author addAuthor(final String[] authorName, final Realm realm) {
        Author author = realm.createObject(Author.class);
        author.setId(realm.allObjects(Author.class).size());
        author.setName(authorName[0]);
        author.setLastname(authorName[1]);
        return author;
    }

    private Publisher createOrGetPublisher(final String publisher, final Book book, final Realm realm) {
        Publisher foundPublisher = getPublisher(publisher, realm);
        if(foundPublisher == null) {
            foundPublisher = addPublisher(publisher, realm);
        }
        foundPublisher.getBooks().add(book);
        return foundPublisher;
    }

    private Publisher addPublisher(final String publisherName, final Realm realm) {
        Publisher publisher = realm.createObject(Publisher.class);
        publisher.setId(realm.allObjects(Publisher.class).size());
        publisher.setName(publisherName);
        return publisher;
    }

    private Author getAuthor(final String[] authorName, final Realm realm) {
        return realm.where(Author.class).equalTo("name", authorName[0]).equalTo("lastname", authorName[1]).findFirst();
    }

    private String[] splitAuthorName(final String author) {
        return author.split(" ");
    }

    private Publisher getPublisher(final String publisher, final Realm realm) {
        return realm.where(Publisher.class).equalTo("name", publisher).findFirst();
    }*/

    public interface OnTransactionCallback {
        void onRealmSuccess();
        void onRealmError(final Exception e);
    }
}
