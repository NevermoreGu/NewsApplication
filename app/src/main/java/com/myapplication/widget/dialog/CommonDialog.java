//package com.myapplication.widget.dialog;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.cpsdna.mirror.R;
//
//public class CommonDialog extends Dialog {
//
//    public CommonDialog(Context context) {
//        super(context);
//    }
//
//    public CommonDialog(Context context, int theme) {
//        super(context, theme);
//    }
//
//    public static class Builder {
//        private Context context;
//        private String title;
//        private String message;
//        private int imgId = 0;
//        private String positiveButtonText;
//        private String negativeButtonText;
//        private boolean leftVisible = false;
//        private View contentView;
//        private OnClickListener positiveButtonClickListener;
//        private OnClickListener negativeButtonClickListener;
//
//        public Builder(Context context) {
//            this.context = context;
//        }
//
//        public Builder setMessage(String message) {
//            this.message = message;
//            return this;
//        }
//
//        public Builder setMessage(int message) {
//            this.message = (String) context.getText(message);
//            return this;
//        }
//
//        public Builder setImage(int resId) {
//            this.imgId = resId;
//            return this;
//        }
//
//        public Builder setTitle(int title) {
//            this.title = (String) context.getText(title);
//            return this;
//        }
//
//        public Builder setTitle(String title) {
//            this.title = title;
//            return this;
//        }
//
//        public Builder setContentView(View v) {
//            this.contentView = v;
//            return this;
//        }
//
//        public Builder setPositiveButton(int positiveButtonText,
//                                         OnClickListener listener) {
//            this.positiveButtonText = (String) context
//                    .getText(positiveButtonText);
//            this.positiveButtonClickListener = listener;
//            return this;
//        }
//
//        public Builder setPositiveButton(String positiveButtonText,
//                                         OnClickListener listener) {
//            this.positiveButtonText = positiveButtonText;
//            this.positiveButtonClickListener = listener;
//            return this;
//        }
//
//        public Builder setNegativeButton(int negativeButtonText,
//                                         OnClickListener listener) {
//            this.negativeButtonText = (String) context
//                    .getText(negativeButtonText);
//            this.negativeButtonClickListener = listener;
//            return this;
//        }
//
//        public Builder setNegativeButton(String negativeButtonText,
//                                         OnClickListener listener) {
//            this.negativeButtonText = negativeButtonText;
//            this.negativeButtonClickListener = listener;
//            return this;
//        }
//
//        public Builder setNegativeButtonVisible(String negativeButtonText,
//                                                OnClickListener listener, Boolean visible) {
//            this.negativeButtonText = negativeButtonText;
//            this.negativeButtonClickListener = listener;
//            this.leftVisible = visible;
//            return this;
//        }
//
//        public CustomAlertDialog create() {
//            LayoutInflater inflater = (LayoutInflater)
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            // instantiate the dialog with the custom Theme
//            final CustomAlertDialog dialog = new CustomAlertDialog(context,
//                    R.style.Dialog);
//            View layout = inflater.inflate(R.layout.dialog_normal_layout, null);
//            dialog.addContentView(layout, new LayoutParams(
//                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//            // set the dialog title
//            ((TextView) layout.findViewById(R.id.title)).setText(title);
//            // set the confirm button
//
//            if (positiveButtonText != null) {
//                ((Button) layout.findViewById(R.id.positiveButton))
//                        .setText(positiveButtonText);
//                if (positiveButtonClickListener != null) {
//                    ((Button) layout.findViewById(R.id.positiveButton))
//                            .setOnClickListener(new View.OnClickListener() {
//                                public void onClick(View v) {
//                                    positiveButtonClickListener.onClick(dialog,
//                                            DialogInterface.BUTTON_POSITIVE);
//                                }
//                            });
//                }
//            } else {
//                // if no confirm button just set the visibility to GONE
//                layout.findViewById(R.id.positiveButton).setVisibility(
//                        View.GONE);
//            }
//            // set the cancel button
//            if (negativeButtonText != null) {
//                ((Button) layout.findViewById(R.id.negativeButton))
//                        .setText(negativeButtonText);
//                if (negativeButtonClickListener != null) {
//                    ((Button) layout.findViewById(R.id.negativeButton))
//                            .setOnClickListener(new View.OnClickListener() {
//                                public void onClick(View v) {
//                                    negativeButtonClickListener.onClick(dialog,
//                                            DialogInterface.BUTTON_NEGATIVE);
//                                }
//                            });
//                    if (leftVisible) {
//                        ((Button) layout.findViewById(R.id.negativeButton))
//                                .setVisibility(View.GONE);
//                    }
//                }
//            } else {
//                // if no confirm button just set the visibility to GONE
//                layout.findViewById(R.id.negativeButton).setVisibility(
//                        View.GONE);
//            }
//            // set the content message
//            if (message != null) {
//                ((TextView) layout.findViewById(R.id.message)).setText(message);
//            } else if (contentView != null) {
//                // if no message set
//                // add the contentView to the dialog body
//                ((LinearLayout) layout.findViewById(R.id.content))
//                        .removeAllViews();
//                ((LinearLayout) layout.findViewById(R.id.content)).addView(
//                        contentView, new LayoutParams(
//                                LayoutParams.MATCH_PARENT,
//                                LayoutParams.MATCH_PARENT));
//            }
//            if (imgId == 0) {
//                ((ImageView) layout.findViewById(R.id.img_message)).setVisibility(View.GONE);
//            } else {
//                ((ImageView) layout.findViewById(R.id.img_message)).setImageResource(imgId);
//            }
//            dialog.setCancelable(false);
//            dialog.setCanceledOnTouchOutside(true);
//            dialog.setContentView(layout);
//
//
//            return dialog;
//        }
//    }
//}
