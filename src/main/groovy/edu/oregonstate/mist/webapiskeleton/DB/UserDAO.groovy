package edu.oregonstate.mist.webapiskeleton.DB

import edu.oregonstate.mist.webapiskeleton.core.User;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;
/**
 * Created by georgecrary on 6/27/15.
 */
class UserDAO extends AbstractDAO<User> {
  public UserDao(SessionFactory factory) {
    super(factory);
  }

  public Optional<User> findById(Long id) {
    return Optional.fromNullable(get(id));
  }

  public User create(User person) {
    return persist(person);
  }

  public List<User> findAll() {
    return list(namedQuery("edu.oregonstate.mist.webapiskeleton.core.User"));
  }
}