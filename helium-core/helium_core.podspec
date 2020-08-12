Pod::Spec.new do |spec|
    spec.name                     = 'helium_core'
    spec.version                  = '0.6.0'
    spec.homepage                 = 'https://github.com/joaquimverges/Helium'
    spec.source                   = { :git => "Not Published", :tag => "Cocoapods/#{spec.name}/#{spec.version}" }
    spec.authors                  = ''
    spec.license                  = ''
    spec.summary                  = 'CocoaPod for Helium Core on iOS'

    spec.static_framework         = true
    spec.vendored_frameworks      = "build/cocoapods/framework/HeliumCore.framework"
    spec.libraries                = "c++"
    spec.module_name              = "HeliumSwiftUI"

    spec.ios.deployment_target  = '9.0'
    spec.swift_version = '4.2'

    spec.subspec 'HeliumSwiftUI' do |sp|
        sp.source_files = "src/iosMain/swift/**/*.{h,m,swift}"
    end

    spec.pod_target_xcconfig = {
        'KOTLIN_TARGET[sdk=iphonesimulator*]' => 'ios_x64',
        'KOTLIN_TARGET[sdk=iphoneos*]' => 'ios_arm',
        'KOTLIN_TARGET[sdk=watchsimulator*]' => 'watchos_x86',
        'KOTLIN_TARGET[sdk=watchos*]' => 'watchos_arm',
        'KOTLIN_TARGET[sdk=appletvsimulator*]' => 'tvos_x64',
        'KOTLIN_TARGET[sdk=appletvos*]' => 'tvos_arm64',
        'KOTLIN_TARGET[sdk=macosx*]' => 'macos_x64'
    }

    spec.script_phases = [
        {
            :name => 'Build helium_core',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                set -ev
                export JAVA_HOME="/Applications/Android Studio.app/Contents/jre/jdk/Contents/Home"
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/../gradlew" -p "$REPO_ROOT" :helium-core:syncFramework \
                    -Pkotlin.native.cocoapods.target=$KOTLIN_TARGET \
                    -Pkotlin.native.cocoapods.configuration=$CONFIGURATION \
                    -Pkotlin.native.cocoapods.cflags="$OTHER_CFLAGS" \
                    -Pkotlin.native.cocoapods.paths.headers="$HEADER_SEARCH_PATHS" \
                    -Pkotlin.native.cocoapods.paths.frameworks="$FRAMEWORK_SEARCH_PATHS"
            SCRIPT
        }
    ]
end