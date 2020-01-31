package org.apache.dubbo.config.spring.status;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.common.status.Status;
import org.apache.dubbo.common.status.StatusChecker;
import org.apache.dubbo.config.spring.extension.SpringExtensionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.Lifecycle;
import org.springframework.web.context.support.GenericWebApplicationContext;
import java.lang.reflect.Method;
/** 
 * SpringStatusChecker
 */
@Activate public class SpringStatusChecker implements StatusChecker {
  private static Logger logger=LoggerFactory.getLogger(SpringStatusChecker.class);
  @Override public Status check(){
    ApplicationContext context=null;
    for (    ApplicationContext c : SpringExtensionFactory.getContexts()) {
      if (c instanceof GenericWebApplicationContext) {
        continue;
      }
      if (c != null) {
        context=c;
        break;
      }
    }
    if (context == null) {
      return new Status(Status.Level.UNKNOWN);
    }
    Status.Level level=Status.Level.OK;
    if (context instanceof Lifecycle) {
      if (((Lifecycle)context).isRunning()) {
        level=Status.Level.OK;
      }
 else {
        level=Status.Level.ERROR;
      }
    }
 else {
      level=Status.Level.UNKNOWN;
    }
    StringBuilder buf=new StringBuilder();
    try {
      Class<?> cls=context.getClass();
      Method method=null;
      while (cls != null && method == null) {
        try {
          method=cls.getDeclaredMethod("getConfigLocations",new Class<?>[0]);
        }
 catch (        NoSuchMethodException t) {
          cls=cls.getSuperclass();
        }
      }
      if (method != null) {
        if (!method.isAccessible()) {
          method.setAccessible(true);
        }
        String[] configs=(String[])method.invoke(context,new Object[0]);
        if (configs != null && configs.length > 0) {
          for (          String config : configs) {
            if (buf.length() > 0) {
              buf.append(",");
            }
            buf.append(config);
          }
        }
      }
    }
 catch (    Throwable t) {
      logger.warn(t.getMessage(),t);
    }
    return new Status(level,buf.toString());
  }
}
