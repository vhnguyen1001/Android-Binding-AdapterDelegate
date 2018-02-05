/*
 *  Copyright Roman Donchenko. All Rights Reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package hiennguyen.me.bindingadaptersample.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.drextended.actionhandler.listener.ActionClickListener;

/**
 * All Binding Adapters and converters in one place
 */
public class Converters {

    @BindingAdapter(value = {"glidePath", "glidePlaceholder", "glideSignature", "glideCacheStrategy", "glideCrossFadeDisabled", "glideAnimation", "glideTransform"}, requireAll = false)
    public static void setImageUri(ImageView imageView, String path, Drawable placeholder, String glideSignature, String glideCacheStrategy, boolean crossFadeDisabled, Integer animationResId, String glideTransform) {
        Context context = imageView.getContext();

        if (context instanceof Activity && ((Activity) context).isFinishing()) return;
        if (context instanceof ContextWrapper) {
            final Context baseContext = ((ContextWrapper) context).getBaseContext();
            if (baseContext instanceof Activity && ((Activity) baseContext).isFinishing()) return;
        }
        boolean isEmptyPath = TextUtils.isEmpty(path);
        if (isEmptyPath) {
            if (placeholder != null) {
                imageView.setImageDrawable(placeholder);
            }
            return;
        }
//        try {
//            RequestOptions request = new RequestOptions();
//
//            if (placeholder != null) {
//                if (!crossFadeDisabled && animationResId == null) request.crossFade();
//                request.placeholder(placeholder);
//            }
//            if (animationResId != null) {
//                request.animate(animationResId);
//            }
//            if (!TextUtils.isEmpty(glideSignature)) {
//                request.signature(new StringSignature(glideSignature));
//            }
//            if (glideTransform != null) {
//                switch (glideTransform) {
//                    case "CIRCLE":
//                        request.bitmapTransform(
//                                new CircleBorderedTransform(Glide.get(context).getBitmapPool(), Color.WHITE));
//                        break;
//                    case "BLUR":
//                        break;
//                }
//            }
//
//            if (!TextUtils.isEmpty(glideCacheStrategy)) {
//                request.diskCacheStrategy(DiskCacheStrategy.valueOf(glideCacheStrategy));
//            }
//
//            request.into(imageView);
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        }
    }

    @BindingAdapter(
            value = {"handler"},
            requireAll = false
    )
    public static void setActionHandler(final View view, final ActionClickListener actionHandler) {
        if(actionHandler != null) {
            view.setOnClickListener(v -> {});
        }

    }
}
