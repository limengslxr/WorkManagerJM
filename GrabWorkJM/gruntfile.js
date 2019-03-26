module.exports = function(grunt) {
    'use strict';

    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        jshint: {
            src: [
                'src/**/*.js',
                '!src/assets/vendor/**/*.js'
            ],
            options: {
                jshintrc: '.jshintrc'
            }
        },
        copy: {
            html: {
                expand: true,
                cwd: 'src',
                src: '**',
                dest: 'dist/'
            }
        },
        clean: {
            tmp: ['.tmp'],
            dist: ['dist']
        },
        useminPrepare: {
            html: 'dist/app.html',
            options: {
                dest: 'dist'
            }
        },
        usemin: {
            html: 'dist/app.html',
            options: {
                dest: 'dist'
            }
        },
        uglify: {
            options: {
                stripBanners: true,
                report: 'min'
            }
        },
        concat: {
            options: {
                banner: "",
                footer: "",
                separator: "\n",
            }
        },
        watch: {
            js: {
                files: ['src/**/*.js'],
                tasks: ['jshint']
            },
            livereload: {
                options: {
                    livereload: '<%= connect.options.livereload %>'
                },
                files: [
                    'src/**/*.js',
                    'src/**/*.html',
                    'src/**/*.css',
                    'src/**/*.{png,jpg,jpeg,gif,webp,svg}'
                ]
            }
        },
        connect: {
            options: {
                port: 9030,
                hostname: 'localhost',
                livereload: 35729
            },
            server: {
                options: {
                    open: 'http://localhost:9030/app.m.html',
                    debug: true,
                    base: [
                        'src'
                    ]
                }
            }
        },
    });

    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-connect');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-usemin');

    grunt.registerTask('dev', ['connect', 'watch']);
    grunt.registerTask('build', ['clean:dist', 
                                 'copy:html', 
                                 'useminPrepare', 
                                 'concat', 
                                 'uglify', 
                                 'usemin', 
                                 'clean:tmp']);
};