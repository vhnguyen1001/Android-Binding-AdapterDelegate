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
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.signature.ObjectKey;

import hiennguyen.me.bindingadapterdelegate.actionhandler.listener.ActionClickListener;

/**
 * All Binding Adapters and converters in one place
 */
public class Converters {

    @BindingAdapter(value = {"glidePath", "glidePlaceholder", "glideSignature", "glideCacheStrategy", "glideCrossFadeDisabled",
            "glideAnimation", "glideTransform"}, requireAll = false)
    public static void setImageUri(ImageView imageView, String path, Drawable placeholder, String glideSignature, DiskCacheStrategy glideCacheStrategy,
                                   boolean crossFadeDisabled, Integer animationResId, String glideTransform) {
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
        try {
            GlideRequest<Drawable> requests = GlideApp.with(context).load(path);

            if (placeholder != null) {
                if (!crossFadeDisabled && animationResId == null) {
                    requests.transition(new DrawableTransitionOptions().crossFade());
                }
                requests.placeholder(placeholder);
            }
            if (animationResId != null) {
                requests.transition(GenericTransitionOptions.with(animationResId));
            }
            if (!TextUtils.isEmpty(glideSignature)) {
                requests.signature(new ObjectKey(glideSignature));
            }
            if (glideTransform != null) {
                switch (glideTransform) {
                    case "CIRCLE":
                        requests.transform(new CircleCrop());
                        break;
                    case "BLUR":
                        break;
                }
            }

            if(glideCacheStrategy != null) {
                requests.diskCacheStrategy(glideCacheStrategy);
            }

            requests.into(imageView);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
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
