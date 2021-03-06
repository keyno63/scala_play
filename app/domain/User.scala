package domain

import scalikejdbc._
import java.time._

import auth.SampleAuth

case class User(
                 id: Int,
                 name: String,
                 email: String,
                 authLevel: SampleAuth,
                 password: String,
                 createAt: ZonedDateTime) {
  // TODO: さらにあとで時刻にする
}

object User extends SQLSyntaxSupport[User]{
  override val tableName = "users"

  def apply(rs: WrappedResultSet) = new User(
    rs.int("id"),
    rs.string("name"),
    rs.string("email"),
    SampleAuth.find(rs.int("authLevel")).get,
    rs.string("password"),
    rs.zonedDateTime("created_at"))

  def create(u: User)(implicit s: DBSession = AutoSession) = {
    sql"""INSERT INTO ${tableName}
         (id, name, email, authLevel, password)
         VALUES
         (${u.id}, ${u.name}, ${u.email}, ${u.authLevel}, ${u.password})""".update.apply()
  }
}
