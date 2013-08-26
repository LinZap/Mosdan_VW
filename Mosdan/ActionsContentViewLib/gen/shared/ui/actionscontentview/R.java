/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * aapt tool from the resource data it found.  It
 * should not be modified by hand.
 */

package shared.ui.actionscontentview;

public final class R {
    public static final class attr {
        /**  Actions layout ID to link at view creation time. 
         <p>Must be a reference to another resource, in the form "<code>@[+][<i>package</i>:]<i>type</i>:<i>name</i></code>"
or to a theme attribute in the form "<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>".
         */
        public static int actions_layout=0x7f010002;
        /**  Content layout ID to link at view creation time. 
         <p>Must be a reference to another resource, in the form "<code>@[+][<i>package</i>:]<i>type</i>:<i>name</i></code>"
or to a theme attribute in the form "<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>".
         */
        public static int content_layout=0x7f010003;
        /**  Spacing value. 
         <p>Must be a dimension value, which is a floating point number appended with a unit such as "<code>14.5sp</code>".
Available units are: px (pixels), dp (density-independent pixels), sp (scaled pixels based on preferred font size),
in (inches), mm (millimeters).
<p>This may also be a reference to a resource (in the form
"<code>@[<i>package</i>:]<i>type</i>:<i>name</i></code>") or
theme attribute (in the form
"<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>")
containing a value of this type.
         */
        public static int spacing=0x7f010001;
        /**  Default spacing types. 
         <p>Must be one of the following constant values.</p>
<table>
<colgroup align="left" />
<colgroup align="left" />
<colgroup align="left" />
<tr><th>Constant</th><th>Value</th><th>Description</th></tr>
<tr><td><code>right_offest</code></td><td>0</td><td> Spacing will be calculated as offset from right bound of view. </td></tr>
<tr><td><code>actions_width</code></td><td>1</td><td> Spacing will be calculated as right bound of actions container. </td></tr>
</table>
         */
        public static int spacing_type=0x7f010000;
    }
    public static final class dimen {
        public static int detault_actionscontentview_spacing=0x7f030000;
    }
    public static final class id {
        public static int actions_width=0x7f020001;
        public static int right_offest=0x7f020000;
    }
    public static final class styleable {
        /** Attributes that can be used with a ActionsContentView.
           <p>Includes the following attributes:</p>
           <table>
           <colgroup align="left" />
           <colgroup align="left" />
           <tr><th>Attribute</th><th>Description</th></tr>
           <tr><td><code>{@link #ActionsContentView_actions_layout shared.ui.actionscontentview:actions_layout}</code></td><td> Actions layout ID to link at view creation time.</td></tr>
           <tr><td><code>{@link #ActionsContentView_content_layout shared.ui.actionscontentview:content_layout}</code></td><td> Content layout ID to link at view creation time.</td></tr>
           <tr><td><code>{@link #ActionsContentView_spacing shared.ui.actionscontentview:spacing}</code></td><td> Spacing value.</td></tr>
           <tr><td><code>{@link #ActionsContentView_spacing_type shared.ui.actionscontentview:spacing_type}</code></td><td> Spacing type.</td></tr>
           </table>
           @see #ActionsContentView_actions_layout
           @see #ActionsContentView_content_layout
           @see #ActionsContentView_spacing
           @see #ActionsContentView_spacing_type
         */
        public static final int[] ActionsContentView = {
            0x7f010000, 0x7f010001, 0x7f010002, 0x7f010003
        };
        /**
          <p>
          @attr description
           Actions layout ID to link at view creation time. 


          <p>Must be a reference to another resource, in the form "<code>@[+][<i>package</i>:]<i>type</i>:<i>name</i></code>"
or to a theme attribute in the form "<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>".
          <p>This is a private symbol.
          @attr name android:actions_layout
        */
        public static final int ActionsContentView_actions_layout = 2;
        /**
          <p>
          @attr description
           Content layout ID to link at view creation time. 


          <p>Must be a reference to another resource, in the form "<code>@[+][<i>package</i>:]<i>type</i>:<i>name</i></code>"
or to a theme attribute in the form "<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>".
          <p>This is a private symbol.
          @attr name android:content_layout
        */
        public static final int ActionsContentView_content_layout = 3;
        /**
          <p>
          @attr description
           Spacing value. 


          <p>Must be a dimension value, which is a floating point number appended with a unit such as "<code>14.5sp</code>".
Available units are: px (pixels), dp (density-independent pixels), sp (scaled pixels based on preferred font size),
in (inches), mm (millimeters).
<p>This may also be a reference to a resource (in the form
"<code>@[<i>package</i>:]<i>type</i>:<i>name</i></code>") or
theme attribute (in the form
"<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>")
containing a value of this type.
          <p>This is a private symbol.
          @attr name android:spacing
        */
        public static final int ActionsContentView_spacing = 1;
        /**
          <p>
          @attr description
           Spacing type. 


          <p>Must be one of the following constant values.</p>
<table>
<colgroup align="left" />
<colgroup align="left" />
<colgroup align="left" />
<tr><th>Constant</th><th>Value</th><th>Description</th></tr>
<tr><td><code>right_offest</code></td><td>0</td><td> Spacing will be calculated as offset from right bound of view. </td></tr>
<tr><td><code>actions_width</code></td><td>1</td><td> Spacing will be calculated as right bound of actions container. </td></tr>
</table>
          <p>This is a private symbol.
          @attr name android:spacing_type
        */
        public static final int ActionsContentView_spacing_type = 0;
    };
}
