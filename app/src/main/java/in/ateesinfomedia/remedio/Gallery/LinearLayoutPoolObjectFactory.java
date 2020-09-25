package in.ateesinfomedia.remedio.Gallery;

import android.content.Context;
import android.widget.LinearLayout;

import com.felipecsl.asymmetricgridview.library.widget.PoolObjectFactory;

public class LinearLayoutPoolObjectFactory implements PoolObjectFactory<LinearLayout> {

  private final Context context;

  public LinearLayoutPoolObjectFactory(final Context context) {
    this.context = context;
  }

  @Override
  public LinearLayout createObject() {
    return new LinearLayout(context, null);
  }
}
