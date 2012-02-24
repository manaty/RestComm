/*
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.mobicents.servlet.sip.restcomm.dao.mongodb;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.mobicents.servlet.sip.restcomm.OutgoingCallerId;
import org.mobicents.servlet.sip.restcomm.Sid;
import org.mobicents.servlet.sip.restcomm.annotations.concurrency.ThreadSafe;
import org.mobicents.servlet.sip.restcomm.dao.OutgoingCallerIdsDao;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

/**
 * @author quintana.thomas@gmail.com (Thomas Quintana)
 */
@ThreadSafe public final class MongoOutgoingCallerIdsDao implements OutgoingCallerIdsDao {
  private static final Logger logger = Logger.getLogger(MongoOutgoingCallerIdsDao.class);
  private final DBCollection collection;

  public MongoOutgoingCallerIdsDao(final DB database) {
    super();
    collection = database.getCollection("restcomm_outgoing_caller_ids");
  }
  
  @Override public void addOutgoingCallerId (final OutgoingCallerId outgoingCallerId) {
    final WriteResult result = collection.insert(toDbObject(outgoingCallerId));
    if(!result.getLastError().ok()) {
      logger.error(result.getLastError().getErrorMessage());
    }
  }

  @Override public OutgoingCallerId getOutgoingCallerId(final Sid sid) {
    final BasicDBObject query = new BasicDBObject();
    query.put("sid", sid.toString());
    final DBObject result = collection.findOne(query);
    if(result != null) {
      return toOutgoingCallerId(result);
    } else {
      return null;
    }
  }

  @Override	public List<OutgoingCallerId> getOutgoingCallerIds(final Sid accountSid) {
	final List<OutgoingCallerId> outgoingCallerIds = new ArrayList<OutgoingCallerId>();
    final BasicDBObject query = new BasicDBObject();
    query.put("account_sid", accountSid.toString());
    final DBCursor results = collection.find(query);
    while(results.hasNext()) {
      outgoingCallerIds.add(toOutgoingCallerId(results.next()));
    }
    return outgoingCallerIds;
  }

  @Override public void removeOutgoingCallerId(final Sid sid) {
    final BasicDBObject query = new BasicDBObject();
    query.put("sid", sid.toString());
    removeOutgoingCallerIds(query);
  }

  @Override public void removeOutgoingCallerIds(final Sid accountSid) {
    final BasicDBObject query = new BasicDBObject();
    query.put("account_sid", accountSid.toString());
    removeOutgoingCallerIds(query);
  }
  
  private void removeOutgoingCallerIds(final DBObject query) {
    final WriteResult result = collection.remove(query);
    if(!result.getLastError().ok()) {
      logger.error(result.getLastError().getErrorMessage());
    }
  }

  @Override public void updateOutgoingCallerId(final OutgoingCallerId outgoingCallerId) {
    final BasicDBObject query = new BasicDBObject();
    query.put("sid", outgoingCallerId.getSid().toString());
    final WriteResult result = collection.update(query, toDbObject(outgoingCallerId));
    if(!result.getLastError().ok()) {
      logger.error(result.getLastError().getErrorMessage());
    }
  }
  
  private DBObject toDbObject(final OutgoingCallerId outgoingCallerId) {
    final BasicDBObject object = new BasicDBObject();
    object.put("sid", outgoingCallerId.getSid().toString());
    object.put("date_created", outgoingCallerId.getDateCreated().toDate());
    object.put("date_updated", outgoingCallerId.getDateUpdated().toDate());
    object.put("friendly_name", outgoingCallerId.getFriendlyName());
    object.put("account_sid", outgoingCallerId.getAccountSid().toString());
    object.put("phone_number", outgoingCallerId.getPhoneNumber());
    object.put("uri", outgoingCallerId.getUri().toString());
    return object;
  }
  
  private OutgoingCallerId toOutgoingCallerId(final DBObject object) {
    final Sid sid = new Sid((String)object.get("sid"));
    final DateTime dateCreated = new DateTime((Date)object.get("date_created"));
    final DateTime dateUpdated = new DateTime((Date)object.get("date_updated"));
    final String friendlyName = (String)object.get("friendly_name");
    final Sid accountSid = new Sid((String)object.get("account_sid"));
    final String phoneNumber = (String)object.get("phone_number");
    final URI uri = URI.create((String)object.get("uri"));
    return new OutgoingCallerId(sid, dateCreated, dateUpdated, friendlyName, accountSid, phoneNumber, uri);
  }
}
