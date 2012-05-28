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
package org.mobicents.servlet.sip.restcomm.http.converter;

import java.net.URI;

import org.mobicents.servlet.sip.restcomm.annotations.concurrency.ThreadSafe;
import org.mobicents.servlet.sip.restcomm.entities.Application;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * @author quintana.thomas@gmail.com (Thomas Quintana)
 */
@ThreadSafe public final class ApplicationConverter extends AbstractConverter {
  public ApplicationConverter() {
    super();
  }
  
  @SuppressWarnings("rawtypes")
  @Override public boolean canConvert(final Class klass) {
    return Application.class.equals(klass);
  }

  @Override public void marshal(final Object object, final HierarchicalStreamWriter writer,
      final MarshallingContext context) {
    final Application application = (Application)object;
    writeSid(application.getSid(), writer);
    writeDateCreated(application.getDateCreated(), writer);
    writeDateUpdated(application.getDateUpdated(), writer);
    writeFriendlyName(application.getFriendlyName(), writer);
    writeAccountSid(application.getAccountSid(), writer);
    writeApiVersion(application.getApiVersion(), writer);
    writeVoiceUrl(application.getVoiceUrl(), writer);
    writeVoiceMethod(application.getVoiceMethod(), writer);
    writeVoiceFallbackUrl(application.getVoiceFallbackUrl(), writer);
    writeVoiceFallbackMethod(application.getVoiceFallbackMethod(), writer);
    writeStatusCallback(application.getStatusCallback(), writer);
    writeStatusCallbackMethod(application.getStatusCallbackMethod(), writer);
    writeVoiceCallerIdLookup(application.hasVoiceCallerIdLookup(), writer);
    writeSmsUrl(application.getSmsUrl(), writer);
    writeSmsMethod(application.getSmsMethod(), writer);
    writeSmsFallbackUrl(application.getSmsFallbackUrl(), writer);
    writeSmsFallbackMethod(application.getSmsFallbackMethod(), writer);
    writeSmsStatusCallback(application.getSmsStatusCallback(), writer);
    writeUri(application.getUri(), writer);
  }
  
  private void writeSmsStatusCallback(final URI smsStatusCallback, final HierarchicalStreamWriter writer) {
    writer.startNode("SmsStatusCallback");
    if(smsStatusCallback != null) {
      writer.setValue(smsStatusCallback.toString());
    }
    writer.endNode();
  }
}